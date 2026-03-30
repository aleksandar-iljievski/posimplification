package se.telenor.posimplification.product.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface UpdateProductInfosActivity {
    void updateProductInfos(String productId);
    void revertInventoryUpdateInfos(String productId);
}
