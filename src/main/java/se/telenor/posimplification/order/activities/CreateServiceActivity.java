package se.telenor.posimplification.order.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface CreateServiceActivity {

    void createService(String serviceId);

}
