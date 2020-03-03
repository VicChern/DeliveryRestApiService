package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Order;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for {@link IOrder}
 */
public interface IOrderService {

    /**
     * Saved new {@link Order} for customer with customerGuid to db
     *
     * @param order        order
     * @param customerGuid customerGuid
     * @return saved order
     */
    IOrder create(IOrder order, UUID customerGuid);

    /**
     * Saved new {@link Order} for customer with customerGuid to db
     *
     * @param orderGuid          orderGuid
     * @param userGuid           userGuid
     * @param orderEventTypeName orderEventTypeName
     * @param payload            payload
     * @return saved order
     */
    IOrderEvent createOrderEvent(UUID orderGuid, UUID userGuid, String orderEventTypeName, String payload);

    /**
     * Gets all orders
     *
     * @return a list of all orders
     */
    List<IOrder> getAll();

    /**
     * Gets order by {@link Order} guid
     *
     * @param guid {@link Order} guid
     * @return guid
     */
    IOrder getByGuid(UUID guid);

    /**
     * Updates {@link Order}
     *
     * @param order
     * @param guid  {@link Order} guid
     * @return updated order
     */
    IOrder update(IOrder order, UUID guid);

    /**
     * Deletes order by {@link Order} guid
     *
     * @param guid
     */
    void deleteByGuid(UUID guid);
}
