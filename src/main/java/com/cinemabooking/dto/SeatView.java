package com.cinemabooking.dto;

/** Mot o ghe tren so do chon ghe: thong tin ghe + da dat hay chua. */
public class SeatView {

    private final Integer seatId;
    private final String seatRow;
    private final Integer seatNumber;
    private final String label;
    private final String seatType;
    private final boolean booked;

    public SeatView(Integer seatId, String seatRow, Integer seatNumber, String label, String seatType, boolean booked) {
        this.seatId = seatId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.label = label;
        this.seatType = seatType;
        this.booked = booked;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public String getLabel() {
        return label;
    }

    public String getSeatType() {
        return seatType;
    }

    public boolean isBooked() {
        return booked;
    }
}
