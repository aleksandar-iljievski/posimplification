package se.telenor.posimplification.order.workflow;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Task {
    private Type type;
    private Status status;

    enum Status {
        PENDING,
        RUNNING,
        COMPLETED
    }

    enum Type {
        CREATE_PRODUCT,
        CREATE_RESOURCE,
        CREATE_SERVICE
    }
}
