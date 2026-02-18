package se.telenor.posimplification.order.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import se.telenor.posimplification.order.activities.CreateProductActivity;

import java.time.Duration;
import java.util.List;

import static se.telenor.posimplification.temporal.TemporalQueues.ORDER_QUEUE;

@WorkflowImpl(taskQueues = ORDER_QUEUE)
public class OrderProcessingWorkflowImpl implements OrderProcessingWorkflow {

    @Override
    public void processOrder() {

        State state = new State();

        while (!state.allTaskCompleted()) {
            var task = state.getReadyTasks().poll(Duration.ofDays(30));

            task.setStatus(Task.Status.RUNNING);

            switch (task.getType()) {
                case CREATE_PRODUCT -> {
                    System.out.println("Creating product");
                    var createProductActivity = Workflow.newActivityStub(CreateProductActivity.class,
                            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofMinutes(1)).build());

                    createProductActivity.createProduct(((CreateProductTask) task).getProductId());
                }
                case CREATE_RESOURCE -> {
                    System.out.println("Creating resource");
                }
                case CREATE_SERVICE -> {
                    System.out.println("Creating service");
                }
            }

            task.setStatus(Task.Status.COMPLETED);
        }

    }

    static class State {
        List<Task> taskList = List.of(new CreateProductTask("product1"),
                new CreateResourceTask("resource1"));
        WorkflowQueue<Task> workflowQueue = Workflow.newWorkflowQueue(100);

        State() {
            taskList.forEach(task -> {
                workflowQueue.offer(task);
            });
        }

        WorkflowQueue<Task> getReadyTasks() {
            return workflowQueue;
        }

        public boolean allTaskCompleted() {
            return taskList.stream().allMatch(t -> t.getStatus() == Task.Status.COMPLETED);
        }
    }



}

