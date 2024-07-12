package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsBySeatSeatId(Long seatId);

    boolean existsByBookedRoomInfoBookedRoomInfoId(Long bookedRoomInfoId);

    @Query(
            value = "select b from Booking b where b.user.userId = :userId and " +
                    "b.seat is not null and b.status = 'beforeEnteringRoom' or b.status = 'enteringRoom'"

    )
    List<Booking> getAvailableSeats(@Param(value = "userId") Long userId);

    @Query(
            value = "select b from Booking b where b.user.userId = :userId and " +
                    "b.seat is null and b.status = 'beforeEnteringRoom' or b.status = 'enteringRoom'"

    )
    List<Booking> getAvailableRooms(@Param(value = "userId") Long userId);
}
