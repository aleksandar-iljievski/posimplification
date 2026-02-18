package se.telenor.posimplification.order.workflow;

import io.temporal.workflow.Workflow;
import lombok.Data;

@Data
public abstract class Task {
    private Type type;
    private Status status = Status.PENDING;
    private String id = Workflow.randomUUID().toString();

    Task(Type type){
        this.type = type;
    }



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
