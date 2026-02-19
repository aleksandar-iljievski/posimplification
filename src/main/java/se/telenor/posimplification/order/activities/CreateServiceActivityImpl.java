package se.telenor.posimplification.order.activities;

import io.temporal.spring.boot.ActivityImpl;
import org.springframework.stereotype.Service;

import static se.telenor.posimplification.temporal.TemporalQueues.ORDER_QUEUE;

@Service
@ActivityImpl(taskQueues = ORDER_QUEUE)
public class CreateServiceActivityImpl implements CreateServiceActivity {



    @Override
    public void createService(String serviceId) {
        System.out.println("Calling external api to create service: " + serviceId);
    }
}
