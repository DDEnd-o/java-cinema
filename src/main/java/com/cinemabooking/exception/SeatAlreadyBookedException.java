package com.cinemabooking.exception;

/** Nem ra khi 1 hoac nhieu ghe da duoc dat cho cung suat chieu (FR-12 / TC-04). */
public class SeatAlreadyBookedException extends RuntimeException {

    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}
