package se.telenor.posimplification.order.workflow;

import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;

import java.util.List;

public class DummyStateImpl implements State {
    List<Action> actionList ;


    boolean isCancelled = false;

    DummyStateImpl() {
        var deleteProduct = new CreateResourceAction("resource1");
        var createProduct = new CreateProductAction("product1");
        createProduct.getDependencies().add(deleteProduct);
        actionList =List.of(deleteProduct, createProduct, new CreateServiceAction("service1"));
    }

    public WorkflowQueue<Action> getReadyTasks() {
        WorkflowQueue<Action> workflowQueue = Workflow.newWorkflowQueue(100);
        actionList.forEach(action -> {
            if (action.isReady()) {
                workflowQueue.offer(action);
            }
        });
        return workflowQueue;
    }

    public boolean allTaskStarted() {
        return actionList.stream().allMatch(t -> t.getStatus() != Action.Status.PENDING);
    }

    public void setTaskCompleted(String taskId) {
        actionList.stream().filter(step -> step.getId().equalsIgnoreCase(taskId))
                .findFirst().ifPresent(t -> t.setStatus(Action.Status.COMPLETED));

    }

    public boolean isCancellable() {
        return true;
    }

    public void cancel() {
        // set all tasks to cancelled
        isCancelled = true;
    }
}
