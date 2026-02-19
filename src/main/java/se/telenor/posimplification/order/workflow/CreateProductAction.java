package se.telenor.posimplification.order.workflow;

import lombok.Getter;

@Getter
public class CreateProductAction extends Action {
    private String productId;
    public CreateProductAction(String productId) {
        super(Type.CREATE_PRODUCT);
        this.productId = productId;
    }
}
