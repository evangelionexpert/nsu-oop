package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaStatusPrinterService {
    void printStatus(Order order);
}