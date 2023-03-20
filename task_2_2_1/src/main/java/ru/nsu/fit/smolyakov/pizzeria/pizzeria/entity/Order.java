package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity;

public class Order {
    public enum Status {
        CREATED("Created"),
        BEING_BAKED("Being baked"),
        WAITING_FOR_DELIVERY("Waiting for delivery"),
        IN_DELIVERY("In delivery"),
        DONE("Delivered");

        private final String caption;

        Status(String caption) {
            this.caption = caption;
        }

        public String getCaption() {
            return caption;
        }
    }

    private final int id;
    private final OrderDescription orderDescription;
    private Status status = Status.CREATED;

    public Order(int id, Status status, OrderDescription orderDescription) {
        this.status = status;
        this.id = id;
        this.orderDescription = orderDescription;
    }

    public static Order create(int id, OrderDescription orderDescription) {
        return new Order(id, Status.CREATED, orderDescription);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public OrderDescription getOrderDescription() {
        return orderDescription;
    }
}
