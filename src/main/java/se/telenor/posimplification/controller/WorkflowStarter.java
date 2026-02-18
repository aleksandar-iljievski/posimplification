package se.telenor.posimplification.controller;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import se.telenor.posimplification.order.workflow.OrderProcessingWorkflow;
import se.telenor.posimplification.temporal.TemporalQueues;

@RequiredArgsConstructor
@RestController
public class WorkflowStarter {

    private final WorkflowClient workflowClient;

    @GetMapping("/start-workflow")
    public void startWorkflow(){
        var orderProcessingWorkflow = workflowClient.newWorkflowStub(OrderProcessingWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId("workflow1")
                        .setTaskQueue(TemporalQueues.ORDER_QUEUE).build());

        WorkflowClient.start(orderProcessingWorkflow::processOrder);

    }

    @GetMapping("/fake-rabbit/{taskId}")
    public void continueProcessing(@PathVariable String taskId){
        var orderProcessingWorkflow =
                workflowClient.newWorkflowStub(OrderProcessingWorkflow.class, "workflow1");

        orderProcessingWorkflow.signalResourceOrderCompleted(new OrderProcessingWorkflow.ResourceOrderArg(taskId, "1"));

    }
}
