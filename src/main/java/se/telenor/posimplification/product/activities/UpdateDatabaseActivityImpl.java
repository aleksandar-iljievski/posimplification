package se.telenor.posimplification.product.activities;

import io.temporal.spring.boot.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static se.telenor.posimplification.temporal.TemporalQueues.ORDER_QUEUE;

@Service
@ActivityImpl(taskQueues = ORDER_QUEUE)
public class UpdateDatabaseActivityImpl implements UpdateDatabaseActivity {

    private static final Logger log = LoggerFactory.getLogger(UpdateDatabaseActivityImpl.class);

    @Override
    public boolean updateDatabase(String productId) {
        log.info("Updating database for product {}", productId);
        // call database API
        // if(error) return false
        // else true
        return false;
    }
}
