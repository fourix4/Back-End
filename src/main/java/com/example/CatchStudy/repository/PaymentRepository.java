package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(Long paymentId);

    void deleteByPaymentId(Long paymentId);

    Optional<Payment> findByBookingBookingId(Long bookingId);

    Page<Payment> findByBooking_User_UserIdAndPaymentTimeBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    List<Payment> findTop30ByBooking_User_UserIdOrderByPaymentTimeDesc(Long userId);

    @Query(
            value = "select p from Payment p join fetch p.booking " +
                    "where p.booking.user.userId = :userId " +
                    "order by p.paymentTime desc limit 30"
    )
    List<Payment> find30PaymentFetchJoin(Long userId);

    @Query(
            value = "select p from Payment p join fetch p.booking " +
                    "where p.booking.user.userId = :userId and " +
                    "p.paymentTime between :startTime and :endTime"
    )
    Page<Payment> findBookingPaymentTimeBetweenFetchJoin(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime,
                                                         @Param("endTime") LocalDateTime endTime, Pageable pageable);

    @Query("SELECT SUM(p.amount) FROM Payment p " +
            "WHERE p.booking.studyCafe.cafeId = :cafeId AND p.paymentTime >= :startOfDay " +
            "AND p.paymentTime < :endOfDay AND p.paymentStatus = 'APPROVE'")
    Integer findTotalSalesByCafeIdAndDate(
            @Param("cafeId") Long cafeId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);
}
