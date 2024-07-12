package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.global.enums.BookingStatus;
import com.example.CatchStudy.global.enums.SeatType;
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

    @OneToOne(orphanRemoval = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "booked_room_info_id")
    private BookedRoomInfo bookedRoomInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private Booking(Integer time, Users user, StudyCafe studyCafe, Seat seat) {
        this.time = time;
        this.user = user;
        this.studyCafe = studyCafe;
        this.seat = seat;
    }
    private Booking(Users user, Integer time, StudyCafe studyCafe, BookedRoomInfo bookedRoomInfo,LocalDateTime startTime,LocalDateTime endTime) {
        this.user = user;
        this.time = time;
        this.studyCafe = studyCafe;
        this.bookedRoomInfo = bookedRoomInfo;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public static Booking of(Integer time, Users user, StudyCafe studyCafe, Seat seat) {
        return new Booking(time, user, studyCafe, seat);
    }

    public static Booking of(Users user, Integer time, StudyCafe studyCafe, BookedRoomInfo bookedRoomInfo,LocalDateTime startTime,LocalDateTime endTime) {
        return new Booking(user, time,studyCafe, bookedRoomInfo,startTime,endTime);
    }

    public void completeBooking(BookingStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    public void cancelBooking(BookingStatus status){
        this.status = status;
        this.startTime = null;
        this.endTime = null;
        this.code = null;
    }
    public SeatType getSeatType(){
        if(this.seat == null){
            return SeatType.room;
        }
        return SeatType.seat;
    }

    public void checkInSeatBooking(LocalDateTime startTime,Integer time){
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(time);
        this.status = BookingStatus.enteringRoom;
    }
    public void deleteRoomInfo(){
        this.bookedRoomInfo = null;
    }

}
