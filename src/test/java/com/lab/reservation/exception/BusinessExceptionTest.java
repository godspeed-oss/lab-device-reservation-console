package com.lab.reservation.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessExceptionTest {
    @Test
    void shouldSaveErrorMessage() {
        BusinessException exception = new BusinessException("设备不存在");

        assertEquals("设备不存在", exception.getMessage());
    }
}