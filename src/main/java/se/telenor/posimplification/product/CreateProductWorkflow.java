package se.telenor.posimplification.product;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import se.telenor.posimplification.order.workflow.OrderProcessingWorkflow;

@WorkflowInterface
public interface CreateProductWorkflow {

    @WorkflowMethod
    CreateProductResult createProduct(String productId);

    @SignalMethod
    void signalManuallyCreatedInDatabaseBySupport();
}
