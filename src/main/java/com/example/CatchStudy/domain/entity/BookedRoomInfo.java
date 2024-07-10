package com.example.CatchStudy.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booked_room_info")
public class BookedRoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookedRoomInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    @Column
    private LocalTime startTime;
    @Column
    private LocalTime endTime;
    @Column
    private LocalDate bookingDate;
    @Column
    private LocalDateTime bookingDateStartTime;
    @Column
    private LocalDateTime bookingDateEndTime;

    private BookedRoomInfo(Room room, LocalTime startTime, LocalTime endTime, LocalDate bookingDate, LocalDateTime bookingDateStartTime, LocalDateTime bookingDateEndTime) {
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookingDate = bookingDate;
        this.bookingDateStartTime = bookingDateStartTime;
        this.bookingDateEndTime = bookingDateEndTime;
    }

    public static BookedRoomInfo of(Room room, LocalTime startTime, LocalTime endTime, LocalDate bookingDate, LocalDateTime bookingDateStartTime, LocalDateTime bookingDateEndTime) {
        return new BookedRoomInfo(room, startTime, endTime, bookingDate, bookingDateStartTime, bookingDateEndTime);
    }
}
