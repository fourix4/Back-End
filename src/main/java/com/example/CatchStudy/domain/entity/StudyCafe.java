package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.domain.dto.request.StudyCafeRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "studycafe")
public class StudyCafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cafeId;

    @Column
    private String cafeName;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String town;

    @Column
    private LocalTime openingHours;

    @Column
    private LocalTime closedHours;

    @Column(columnDefinition = "TEXT")
    private String closedDay;

    @Column
    private String cafePhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    public StudyCafe(StudyCafeRequestDto studyCafeRequestDto,LocalTime openingHours, LocalTime closedHours, Users user) {
        this.cafeName = studyCafeRequestDto.getCafeName();
        this.address = studyCafeRequestDto.getAddress().toAddress();
        this.city = studyCafeRequestDto.getAddress().getCity();
        this.country = studyCafeRequestDto.getAddress().getCountry();
        this.town = studyCafeRequestDto.getAddress().getTown();
        this.openingHours = openingHours;
        this.closedHours = closedHours;
        this.closedDay = studyCafeRequestDto.getClosedDay();
        this.cafePhone = studyCafeRequestDto.getCafePhone();
        this.user = user;
    }
}
