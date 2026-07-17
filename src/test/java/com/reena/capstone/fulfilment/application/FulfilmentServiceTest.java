package com.reena.capstone.fulfilment.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FulfilmentServiceTest {

    private final FulfilmentService fulfilmentService = new FulfilmentServiceImpl();

    @Test
    void shouldFulfilOrderWhenQuantityIsWithinLimit() {
        assertTrue(fulfilmentService.canFulfil(10));
    }

    @Test
    void shouldRejectOrderWhenQuantityExceedsLimit() {
        assertFalse(fulfilmentService.canFulfil(21));
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZero() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> fulfilmentService.canFulfil(0)
        );

        assertEquals(
                "Total order quantity must be greater than zero.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> fulfilmentService.canFulfil(-1)
        );
    }
}