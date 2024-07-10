package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.BookedRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BookedRoomInfoRepository extends JpaRepository<BookedRoomInfo,Long> {

    @Query(
            "select count(*) from BookedRoomInfo b where b.room.roomId = :roomId and " +
            "((b.bookingDateStartTime < :startTime and b.bookingDateEndTime > :startTime) or " +
            "(b.bookingDateStartTime >= :startTime and b.bookingDateStartTime < :endTime))"
    )
    Integer existsBookedRoom(@Param(value = "roomId")Long roomId, @Param(value = "startTime") LocalDateTime startTime, @Param(value = "endTime")LocalDateTime endTime);
}
