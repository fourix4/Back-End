package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findBySeatId(Long seatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Seat s where s.seatId = :seatId")
    Optional<Seat> findBySeatIdLock(@Param("seatId") Long seatId);

    List<Seat> findAllByStudyCafeCafeId(Long cafeId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select s from Seat s where s.seatId = :seatId")
    Optional<Seat> findBySeatIdOptimisticLock(@Param("seatId")Long seatId);

    void deleteAllByStudyCafe_CafeId(long cafeId);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.studyCafe.cafeId = :cafeId")
    int countSeatByStudyCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.studyCafe.cafeId = :cafeId AND s.isAvailable = true")
    int countAvailableSeatsByStudyCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.studyCafe.cafeId = :cafeId AND s.isAvailable = false")
    int countUsingSeatsByStudyCafeId(@Param("cafeId") Long cafeId);
}
