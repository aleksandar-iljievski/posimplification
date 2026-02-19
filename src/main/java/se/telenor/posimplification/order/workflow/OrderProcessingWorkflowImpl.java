package se.telenor.posimplification.order.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.CanceledFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.*;
import se.telenor.posimplification.order.activities.CreateProductActivity;
import se.telenor.posimplification.order.activities.CreateServiceActivity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static se.telenor.posimplification.temporal.TemporalQueues.ORDER_QUEUE;

@WorkflowImpl(taskQueues = ORDER_QUEUE)
public class OrderProcessingWorkflowImpl implements OrderProcessingWorkflow {
    State state = new State();
    Saga saga = new Saga(new Saga.Options.Builder().setParallelCompensation(true).build());

    CreateProductActivity createProductActivity = Workflow.newActivityStub(CreateProductActivity.class,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofMinutes(1)).build());

    CreateServiceActivity createServiceActivity = Workflow.newActivityStub(CreateServiceActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofMinutes(1))
                    .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(2).build())
                    .build());

    CancellationScope cancellationScope;

    List<Promise<Void>> stepExecutions = new ArrayList<>();

    @Override
    public void processOrder() {

        cancellationScope = Workflow.newCancellationScope(() -> {
            while (!state.allTaskStarted()) {
                var task = state.getReadyTasks().poll(Duration.ofDays(30));
                task.setStatus(Step.Status.RUNNING);
                var procedureAsync = Async.procedure(() ->
                {
                    switch (task.getType()) {
                        case CREATE_PRODUCT -> {
                            System.out.println("Creating product");
                            var productId = ((CreateProductStep) task).getProductId();
                            saga.addCompensation(createProductActivity::terminateProductCreation, productId);

                            createProductActivity.createProduct(productId);
                            task.setStatus(Step.Status.COMPLETED);
                        }
                        case CREATE_RESOURCE -> {
                            System.out.println("Creating resource");
                            saga.addCompensation(() -> System.out.println("we are doing compensation stuff on resource"));
                            Workflow.await(() -> task.getStatus().equals(Step.Status.COMPLETED));

                        }
                        case CREATE_SERVICE -> {
                            System.out.println("Creating service");
                            createServiceActivity.createService(((CreateServiceStep) task).getServiceId());
                            task.setStatus(Step.Status.COMPLETED);
                        }
                    }
                });
                stepExecutions.add(procedureAsync);

            }
            Promise.allOf(stepExecutions).get();
        });

        try {
            cancellationScope.run();
        } catch (ActivityFailure | CanceledFailure e) {
            System.out.println("Activity failed, doing compensation");
            saga.compensate();
        }
    }

    public static class State {
        List<Step> stepList = List.of(new CreateProductStep("product1"),
                new CreateResourceStep("resource1"), new CreateServiceStep("service1"));

        WorkflowQueue<Step> workflowQueue = Workflow.newWorkflowQueue(100);

        boolean isCancelled = false;

        State() {
            stepList.forEach(step -> {
                workflowQueue.offer(step);
            });
        }

        WorkflowQueue<Step> getReadyTasks() {
            return workflowQueue;
        }

        public boolean allTaskStarted() {
            return stepList.stream().allMatch(t -> t.getStatus() != Step.Status.PENDING);
        }

        public void setTaskCompleted(String taskId) {
            stepList.stream().filter(step -> step.getId().equalsIgnoreCase(taskId))
                    .findFirst().ifPresent(t -> t.setStatus(Step.Status.COMPLETED));

        }

        public boolean isCancellable() {
            return true;
        }

        public void cancel() {
            // set all tasks to cancelled
            isCancelled = true;
        }
    }

    @Override
    public void signalResourceOrderCompleted(ResourceOrderArg resourceOrderArg) {
        state.setTaskCompleted(resourceOrderArg.externalId());
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public CancellationResult cancelOrderProcessing() {
        if (state.isCancellable()) {
            cancellationScope.cancel();
            state.cancel();
            return CancellationResult.builder().cancelled(true).build();
        }
        return CancellationResult.builder().cancelled(false).build();
    }
}

