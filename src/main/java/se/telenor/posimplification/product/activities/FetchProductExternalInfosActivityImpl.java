package se.telenor.posimplification.product.activities;

import io.temporal.activity.Activity;
import io.temporal.spring.boot.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static se.telenor.posimplification.temporal.TemporalQueues.ORDER_QUEUE;

@Service
@ActivityImpl(taskQueues = ORDER_QUEUE)
public class FetchProductExternalInfosActivityImpl implements FetchProductExternalInfosActivity {

    private static final Logger log = LoggerFactory.getLogger(FetchProductExternalInfosActivityImpl.class);

    @Override
    public void fetchProductExternalInfos(String productId) {
        int attempt = Activity.getExecutionContext().getInfo().getAttempt();
        log.info("Fetching external infos for product {} (attempt {})", productId, attempt);
        if (attempt == 1) {
            throw new RuntimeException("Simulated failure on first attempt");
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Fetch external infos succeeded for product {}", productId);
    }
}
