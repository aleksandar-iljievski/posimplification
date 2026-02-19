package se.telenor.posimplification.order.workflow;

import io.temporal.workflow.Workflow;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Action {
    private Type type;
    private Status status = Status.PENDING;
    private String id = Workflow.randomUUID().toString();
    private List<Action> dependencies = new ArrayList<>();
    Action(Type type){
        this.type = type;
    }

    public boolean isReady() {
        return status == Status.PENDING && dependencies.stream().allMatch(Action::isCompleted);
    }

    public boolean isCompleted() {
        return status == Status.COMPLETED;
    }

    enum Status {
        PENDING,
        RUNNING,
        COMPLETED
    }

    enum Type {
        CREATE_PRODUCT,
        CREATE_RESOURCE,
        CREATE_SERVICE,
        MODIFY_SERVICE,
        MODIFY_RESOURCE,
        PATCH_PRODUCT,
        DELETE_SERVICE,
        DELETE_RESOURCE,
        DELETE_PRODUCT
    }
}