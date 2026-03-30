package se.telenor.posimplification.product;

import static se.telenor.posimplification.temporal.TemporalQueues.ORDER_QUEUE;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import se.telenor.posimplification.product.activities.FetchProductExternalInfosActivity;
import se.telenor.posimplification.product.activities.UpdateDatabaseActivity;
import se.telenor.posimplification.product.activities.UpdateProductInfosActivity;

@WorkflowImpl(taskQueues = ORDER_QUEUE)
public class CreateProductWorkflowImpl implements CreateProductWorkflow {

  FetchProductExternalInfosActivity fetchProductExternalInfosActivity =
      Workflow.newActivityStub(
          FetchProductExternalInfosActivity.class,
          ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofMinutes(1)).build());

  UpdateProductInfosActivity updateInventory =
      Workflow.newActivityStub(
          UpdateProductInfosActivity.class,
          ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofMinutes(1)).build());

  UpdateDatabaseActivity updateDatabaseActivity =
      Workflow.newActivityStub(
          UpdateDatabaseActivity.class,
          ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofMinutes(1)).build());

  Saga saga = new Saga(new Saga.Options.Builder().build());
  Logger logger = Workflow.getLogger(CreateProductWorkflowImpl.class);

  boolean manuallyCreatedInDatabaseBySupport = false;

  @Override
  public CreateProductResult createProduct(String productId) {
    logger.info("I am a child workflow");

    try {
      fetchProductExternalInfosActivity.fetchProductExternalInfos(productId);

      saga.addCompensation(updateInventory::revertInventoryUpdateInfos, productId);
      updateInventory.updateProductInfos(productId);

      String parentWorkflowId = Workflow.getInfo().getParentWorkflowId().orElse("");
      if ("error".equals(parentWorkflowId)) {
        logger.info("Simulation of error inside the child workflow");
        throw new RuntimeException("error creating product");
      }

      boolean isSuccess = updateDatabaseActivity.updateDatabase(productId);

      if (isSuccess) {
        return new CreateProductResult(true, null);
      }

      Workflow.await(() -> manuallyCreatedInDatabaseBySupport);

      return new CreateProductResult(true, null);

    } catch (Exception e) {
      saga.compensate();
      logger.info("Compensation done inside child workflow");
      return new CreateProductResult(false, e.getMessage());
    }
  }

  @Override
  public void signalManuallyCreatedInDatabaseBySupport() {
    manuallyCreatedInDatabaseBySupport = true;
  }


}
