package se.telenor.posimplification.order.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import jakarta.annotation.Resource;
import se.telenor.posimplification.order.activities.CreateProductActivity;

import java.time.Duration;
import java.util.List;

import static se.telenor.posimplification.temporal.TemporalQueues.ORDER_QUEUE;

@WorkflowImpl(taskQueues = ORDER_QUEUE)
public class OrderProcessingWorkflowImpl implements OrderProcessingWorkflow {
    State state = new State();

    @Override
    public void processOrder() {

        while (!state.allTaskCompleted()) {
            var task = state.getReadyTasks().poll(Duration.ofDays(30));

            task.setStatus(Task.Status.RUNNING);

            switch (task.getType()) {
                case CREATE_PRODUCT -> {
                    System.out.println("Creating product");
                    var createProductActivity = Workflow.newActivityStub(CreateProductActivity.class,
                            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofMinutes(1)).build());

                    createProductActivity.createProduct(((CreateProductTask) task).getProductId());
                    task.setStatus(Task.Status.COMPLETED);
                }
                case CREATE_RESOURCE -> {
                    System.out.println("Creating resource");
                    Workflow.await(() -> task.getStatus().equals(Task.Status.COMPLETED));
                }
                case CREATE_SERVICE -> {
                    System.out.println("Creating service");
                }
            }
        }

    }

    public static class State {
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

        public void setTaskCompleted(String taskId) {
            taskList.stream().filter(task -> task.getId().equalsIgnoreCase(taskId))
                    .findFirst().ifPresent(t -> t.setStatus(Task.Status.COMPLETED));

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
}

