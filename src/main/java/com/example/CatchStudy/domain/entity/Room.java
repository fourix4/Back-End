package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.domain.dto.request.RoomsRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column
    private String roomName;

    @Column
    private Integer capacity;

    @Column
    private Long cancelAvailableTime;

    @Column
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private StudyCafe studyCafe;

    public Room(RoomsRequestDto roomsRequestDto, StudyCafe studyCafe, long cancelAvailableTime) {
        this.roomName = roomsRequestDto.getRoomName();
        this.capacity = roomsRequestDto.getCapacity();
        this.price = roomsRequestDto.getPrice();
        this.cancelAvailableTime = cancelAvailableTime;
        this.studyCafe = studyCafe;
    }
}
