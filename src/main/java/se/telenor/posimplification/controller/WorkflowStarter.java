package se.telenor.posimplification.controller;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
                WorkflowOptions.newBuilder().setTaskQueue(TemporalQueues.ORDER_QUEUE).build());

        WorkflowClient.start(orderProcessingWorkflow::processOrder);

    }
}
