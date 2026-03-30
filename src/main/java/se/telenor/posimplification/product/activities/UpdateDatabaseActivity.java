package se.telenor.posimplification.product.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface UpdateDatabaseActivity {
    boolean updateDatabase(String productId);
}
