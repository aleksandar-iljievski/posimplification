package se.telenor.posimplification.order.workflow;

import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyStateImpl implements State {


    List<Action> actionList;
    WorkflowQueue<Action> readyQueue = Workflow.newWorkflowQueue(100);

    boolean isCancelled = false;
    boolean waitForExtranalApprouval = false;

    DummyStateImpl() {
        var deleteProduct = new DeleteProductAction("product0");
        var createProduct = new CreateProductAction("product1");
        createProduct.getDependencies().add(deleteProduct);
        CreateServiceAction serviceAction = new CreateServiceAction("service1");
        actionList = List.of(deleteProduct, createProduct, serviceAction);
        actionList.stream().filter(Action::isReady).forEach(action -> {
            log.info("action is ready: {}", action.getType());
            readyQueue.offer(action);
        });
    }

    public WorkflowQueue<Action> getReadyTasks() {
        return readyQueue;
    }

    public boolean allTaskStarted() {
        return actionList.stream().allMatch(t -> t.getStatus() != Action.Status.PENDING);
    }

    public void setTaskCompleted(String taskId) {
        actionList.stream().filter(step -> step.getId().equalsIgnoreCase(taskId))
                .findFirst().ifPresent(t -> {
                    t.setStatus(Action.Status.COMPLETED);
                    actionList.stream().filter(Action::isReady).forEach(action -> {
                        log.info("action is ready: {}", action.getType());
                        readyQueue.offer(action);
                    });
                });
    }

    public boolean isCancellable() {
        return true;
    }

    public void cancel() {
        // set all tasks to cancelled
        isCancelled = true;
    }
}
