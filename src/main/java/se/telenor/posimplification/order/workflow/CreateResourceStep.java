package se.telenor.posimplification.order.workflow;

import lombok.Getter;

@Getter
public class CreateResourceStep extends Step {
    private String resourceId;
    public CreateResourceStep(String resourceId) {
        super(Type.CREATE_RESOURCE);
        this.resourceId = resourceId;
    }
}
