package se.telenor.posimplification.order.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface OrderProcessingWorkflow {

    @WorkflowMethod
    void processOrder();

    @SignalMethod
    void signalResourceOrderCompleted(ResourceOrderArg resourceOrderArg);

    record ResourceOrderArg(String externalId, String resourceId){}

    @QueryMethod
    OrderProcessingWorkflowImpl.State getState();
}
