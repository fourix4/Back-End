package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.global.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private String code;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column
    private Integer time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private StudyCafe studyCafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private Booking(Integer time, Users user, StudyCafe studyCafe, Seat seat) {
        this.time = time;
        this.user = user;
        this.studyCafe = studyCafe;
        this.seat = seat;
    }

    private Booking(LocalDateTime startTime, LocalDateTime endTime, Users user, StudyCafe studyCafe, Room room) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.studyCafe = studyCafe;
        this.room = room;
    }

    public static Booking of(Integer time, Users user, StudyCafe studyCafe, Seat seat) {
        return new Booking(time, user, studyCafe, seat);
    }

    public static Booking of(LocalDateTime startTime, LocalDateTime endTime, Users user, StudyCafe studyCafe, Room room) {
        return new Booking(startTime, endTime, user, studyCafe, room);
    }
}
