package se.telenor.posimplification.order.workflow;

import lombok.Getter;

@Getter
public class CreateProductTask extends Task {
    private String productId;
    public CreateProductTask(String productId) {
        super(Type.CREATE_PRODUCT, Status.PENDING);
        this.productId = productId;
    }
}
