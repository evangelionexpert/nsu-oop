package ru.nsu.fit.smolyakov.pizzeria.pizzeria.worker.deliveryboy;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

public interface DeliveryBoy {
    void takeOrdersFromWarehouse();
    Order deliverOrder();
}