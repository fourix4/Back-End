package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.domain.dto.request.UsageFeeRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "usage_fee")
public class UsageFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long priceId;

    @Column
    private Integer hours;

    @Column
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private StudyCafe studyCafe;

    public UsageFee(UsageFeeRequestDto usageFeeRequestDto, StudyCafe studyCafe) {
        this.hours = usageFeeRequestDto.getHours();
        this.price = usageFeeRequestDto.getPrice();
        this.studyCafe = studyCafe;
    }
}
