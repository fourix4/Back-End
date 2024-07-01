package com.example.CatchStudy.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "studycafe")
public class StudyCafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cafeNameId;

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

    @Column(columnDefinition = "TEXT")
    private String openingHours;

    @Column(columnDefinition = "TEXT")
    private String closedDay;

    @Column
    private String cafePhone;
}
