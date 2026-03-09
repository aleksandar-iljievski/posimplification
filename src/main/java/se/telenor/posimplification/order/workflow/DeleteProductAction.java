package se.telenor.posimplification.order.workflow;

import lombok.Getter;

@Getter
public class DeleteProductAction extends Action {
    private String productId;
    public DeleteProductAction(String productId) {
        super(Type.DELETE_PRODUCT);
        this.productId = productId;
    }
}
