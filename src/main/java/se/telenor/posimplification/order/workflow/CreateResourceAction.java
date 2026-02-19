package se.telenor.posimplification.order.workflow;

import lombok.Getter;

@Getter
public class CreateResourceAction extends Action {
    private String resourceId;
    public CreateResourceAction(String resourceId) {
        super(Type.CREATE_RESOURCE);
        this.resourceId = resourceId;
    }
}
