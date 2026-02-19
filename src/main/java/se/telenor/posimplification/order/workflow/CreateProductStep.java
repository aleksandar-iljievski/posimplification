package se.telenor.posimplification.order.workflow;

import lombok.Getter;

@Getter
public class CreateProductStep extends Step {
    private String productId;
    public CreateProductStep(String productId) {
        super(Type.CREATE_PRODUCT);
        this.productId = productId;
    }
}
