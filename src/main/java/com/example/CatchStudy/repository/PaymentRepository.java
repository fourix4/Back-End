package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentId(Long paymentId);

    void deleteByPaymentId(Long paymentId);


}
