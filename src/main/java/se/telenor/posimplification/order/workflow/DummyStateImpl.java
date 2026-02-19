package se.telenor.posimplification.order.workflow;

import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;

import java.util.List;

public class DummyStateImpl implements State {
    List<Step> stepList = List.of(new CreateProductStep("product1"),
            new CreateResourceStep("resource1"), new CreateServiceStep("service1"));

    WorkflowQueue<Step> workflowQueue = Workflow.newWorkflowQueue(100);

    boolean isCancelled = false;

    DummyStateImpl() {
        stepList.forEach(step -> {
            workflowQueue.offer(step);
        });
    }

    public WorkflowQueue<Step> getReadyTasks() {
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
