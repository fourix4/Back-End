package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsBySeatSeatId(Long seatId);
    boolean existsByRoomRoomId(Long roomId);
}
