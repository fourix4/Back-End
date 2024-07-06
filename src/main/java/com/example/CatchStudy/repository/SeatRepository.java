package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findBySeatId(Long seatId);

}
