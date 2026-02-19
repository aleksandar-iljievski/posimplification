package se.telenor.posimplification.order.workflow;

import io.temporal.workflow.WorkflowQueue;

public interface State {
    WorkflowQueue<Step> getReadyTasks();

    boolean allTaskStarted();
    void setTaskCompleted(String taskId);
    boolean isCancellable();
    void cancel();
}
