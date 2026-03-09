package se.telenor.posimplification.order.activities;

import static se.telenor.posimplification.temporal.TemporalQueues.ORDER_QUEUE;

import io.temporal.spring.boot.ActivityImpl;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@ActivityImpl(taskQueues = ORDER_QUEUE)
public class DeleteProductActivityImpl implements DeleteProductActivity {

    @Override
    @SneakyThrows
    public void deleteProduct(String productId) {
        System.out.println("Calling external api to delete product: " + productId);
        Thread.sleep(3000);
        System.out.println("Product deletion done: " + productId);
    }
}
