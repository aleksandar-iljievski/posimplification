package se.telenor.posimplification.order.workflow;

import io.temporal.workflow.*;

@WorkflowInterface
public interface OrderProcessingWorkflow {

    @WorkflowMethod
    void processOrder();

    @SignalMethod
    void signalResourceOrderCompleted(ResourceOrderArg resourceOrderArg);

    record ResourceOrderArg(String externalId, String resourceId){}

    @QueryMethod
    State getState();

    @UpdateMethod
    CancellationResult cancelOrderProcessing();
}
