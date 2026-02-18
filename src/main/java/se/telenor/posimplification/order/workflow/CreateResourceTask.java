package se.telenor.posimplification.order.workflow;

import lombok.Getter;

@Getter
public class CreateResourceTask extends Task {
    private String resourceId;
    public CreateResourceTask(String resourceId) {
        super(Type.CREATE_RESOURCE, Status.PENDING);
        this.resourceId = resourceId;
    }
}
