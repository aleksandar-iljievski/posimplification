package se.telenor.posimplification.product.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface FetchProductExternalInfosActivity {
    void fetchProductExternalInfos(String productId);
}
