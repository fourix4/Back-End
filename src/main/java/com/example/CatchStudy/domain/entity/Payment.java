package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.global.enums.PaymentStatus;
import com.example.CatchStudy.global.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.print.Book;
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

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column
    private String tid;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Payment(PaymentType paymentType, Booking booking, PaymentStatus paymentStatus) {
        this.paymentType = paymentType;
        this.booking = booking;
        this.paymentStatus = paymentStatus;
    }


    public static Payment of(PaymentType paymentType, Booking booking, PaymentStatus paymentStatus) {
        return new Payment(paymentType, booking, paymentStatus);
    }

    public void updateTid(String tid) {
        this.tid = tid;
    }

    public void approvePayment(LocalDateTime paymentTime, PaymentStatus paymentStatus, Integer amount) {
        this.paymentTime = paymentTime;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }
}
