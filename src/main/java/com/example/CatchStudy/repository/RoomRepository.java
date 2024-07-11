package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomId(Long roomId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Room r where r.roomId = :roomId ")
    Optional<Room> findByRoomIdLock(Long roomId);

    List<Room> findAllByStudyCafeCafeId(Long cafeId);

    void deleteAllByStudyCafe_CafeId(long cafeId);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.studyCafe.cafeId = :cafeId")
    Integer countRoomByStudyCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.studyCafe.cafeId = :cafeId AND r.roomId NOT IN (" +
            "SELECT b.room.roomId FROM BookedRoomInfo b WHERE b.bookingDate = CURRENT_DATE " +
            "AND b.startTime <= :currentTime AND b.endTime > :currentTime)")
    Integer countAvailableRoomsByStudyCafeId(@Param("cafeId") Long cafeId, @Param("currentTime") LocalTime currentTime);
}
