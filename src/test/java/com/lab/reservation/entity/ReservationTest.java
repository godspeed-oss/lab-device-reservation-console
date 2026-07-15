package com.lab.reservation.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationTest {
    @Test
    void shouldReturnTimeSlot() {
        Reservation reservation = new Reservation(
                1,
                2,
                "张三",
                "2026-07-15",
                "09:00",
                "11:00"
        );

        assertEquals("09:00-11:00", reservation.getTimeSlot());
    }

    @Test
    void shouldUpdateReservationDate() {
        Reservation reservation = new Reservation(
                1,
                2,
                "张三",
                "2026-07-15",
                "09:00",
                "11:00"
        );

        reservation.setReservationDate("2026-07-16");

        assertEquals("2026-07-16", reservation.getReservationDate());
        assertEquals("2026-07-16", reservation.getDate());
    }
}