package se.telenor.posimplification.order.workflow;

import lombok.Getter;

@Getter
public class CreateServiceAction extends Action {
    private String serviceId;
    public CreateServiceAction(String resourceId) {
        super(Type.CREATE_SERVICE);
        this.serviceId = resourceId;
    }
}
