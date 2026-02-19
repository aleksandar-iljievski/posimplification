package se.telenor.posimplification.order.workflow;

import lombok.Getter;

@Getter
public class CreateServiceStep extends Step {
    private String serviceId;
    public CreateServiceStep(String resourceId) {
        super(Type.CREATE_SERVICE);
        this.serviceId = resourceId;
    }
}
