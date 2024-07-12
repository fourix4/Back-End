package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.domain.dto.request.ManagerRequestDto;
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

    public StudyCafe(ManagerRequestDto managerRequestDto, LocalTime openingHours, LocalTime closedHours, Users user) {
        this.cafeName = managerRequestDto.getCafeName();
        this.address = managerRequestDto.getAddress().toAddress();
        this.city = managerRequestDto.getAddress().getCity();
        this.country = managerRequestDto.getAddress().getCountry();
        this.town = managerRequestDto.getAddress().getTown();
        this.openingHours = openingHours;
        this.closedHours = closedHours;
        this.closedDay = managerRequestDto.getClosedDay();
        this.cafePhone = managerRequestDto.getCafePhone();
        this.user = user;
    }

    public void update(ManagerRequestDto managerRequestDto, LocalTime openingHours, LocalTime closedHours) {
        this.cafeName = managerRequestDto.getCafeName();
        this.address = managerRequestDto.getAddress().toAddress();
        this.city = managerRequestDto.getAddress().getCity();
        this.country = managerRequestDto.getAddress().getCountry();
        this.town = managerRequestDto.getAddress().getTown();
        this.openingHours = openingHours;
        this.closedHours = closedHours;
        this.closedDay = managerRequestDto.getClosedDay();
        this.cafePhone = managerRequestDto.getCafePhone();
    }
}
