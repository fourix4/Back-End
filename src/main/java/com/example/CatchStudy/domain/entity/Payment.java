package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.global.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column
    private LocalDateTime paymentTime;

    @Column
    private Integer amount;

    @Enumerated
    private Enum<PaymentType> paymentType;
}
