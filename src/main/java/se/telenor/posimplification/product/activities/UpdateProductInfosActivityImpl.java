package se.telenor.posimplification.product.activities;

import io.temporal.activity.Activity;
import io.temporal.spring.boot.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static se.telenor.posimplification.temporal.TemporalQueues.ORDER_QUEUE;

@Service
@ActivityImpl(taskQueues = ORDER_QUEUE)
public class UpdateProductInfosActivityImpl implements UpdateProductInfosActivity {

    private static final Logger log = LoggerFactory.getLogger(UpdateProductInfosActivityImpl.class);

    @Override
    public void updateProductInfos(String productId) {
        int attempt = Activity.getExecutionContext().getInfo().getAttempt();
        log.info("Updating product infos for product {} (attempt {})", productId, attempt);
        if (attempt == 1) {
            throw new RuntimeException("Simulated failure on first attempt");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Update product infos succeeded for product {}", productId);
    }

    @Override
    public void revertInventoryUpdateInfos(String productId) {
        log.info("Reverting update product infos for product {}", productId);
    }
}
