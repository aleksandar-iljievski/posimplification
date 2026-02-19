package se.telenor.posimplification.order.activities;

import io.temporal.spring.boot.ActivityImpl;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import static se.telenor.posimplification.temporal.TemporalQueues.ORDER_QUEUE;

@Service
@ActivityImpl(taskQueues = ORDER_QUEUE)
public class CreateProductActivityImpl implements CreateProductActivity {

    @Override
    @SneakyThrows
    public void createProduct(String productId) {
        System.out.println("Calling external api to create product: " + productId);
        Thread.sleep(500);
    }

    public void terminateProductCreation(String productId) {
        System.out.println("Terminating product creation for product: " + productId);
    }
}
