package se.telenor.posimplification.order.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CreateProductActivity {

    void createProduct(String productId);
}
