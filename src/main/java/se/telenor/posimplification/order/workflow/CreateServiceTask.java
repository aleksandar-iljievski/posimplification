package se.telenor.posimplification.order.workflow;

import lombok.Getter;

@Getter
public class CreateServiceTask extends Task {
    private String serviceId;
    public CreateServiceTask(String resourceId) {
        super(Type.CREATE_SERVICE);
        this.serviceId = resourceId;
    }
}
