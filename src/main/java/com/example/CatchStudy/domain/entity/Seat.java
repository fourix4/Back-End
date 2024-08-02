package com.example.CatchStudy.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @Column
    private String seatNumber;

    @Column
    private Boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private StudyCafe studyCafe;

    @Version
    private Long version;

    public void updateSeatStatus(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Seat(String seatNumber, StudyCafe studyCafe) {
        this.seatNumber = seatNumber;
        this.isAvailable = false;
        this.studyCafe = studyCafe;
    }

    public void checkOutSeat(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
