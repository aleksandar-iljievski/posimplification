package se.telenor.posimplification.order.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface DeleteProductActivity {

    void deleteProduct(String productId);

}
