package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(Long paymentId);

    void deleteByPaymentId(Long paymentId);

    Optional<Payment> findByBookingBookingId(Long bookingId);

    Page<Payment> findByBooking_User_UserIdAndPaymentTimeBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    List<Payment> findTop30ByBooking_User_UserIdOrderByPaymentTimeDesc(Long userId);


}
