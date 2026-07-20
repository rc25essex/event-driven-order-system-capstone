package com.ordersystem.capstone.fulfilment.application;

import org.springframework.stereotype.Service;

@Service
public class FulfilmentServiceImpl implements FulfilmentService {

    private static final int MAXIMUM_ORDER_QUANTITY = 20;

    /**
     * Determines whether an order can be fulfilled.
     *
     * @param totalQuantity total quantity across all order items
     * @return true when the order is within the fulfilment limit
     */
    public boolean canFulfil(int totalQuantity) {
        if (totalQuantity <= 0) {
            throw new IllegalArgumentException(
                    "Total order quantity must be greater than zero."
            );
        }

        return totalQuantity <= MAXIMUM_ORDER_QUANTITY;
    }
}