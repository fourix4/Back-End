package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.StudyCafe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudyCafeRepository extends JpaRepository<StudyCafe, Long> {

    Optional<StudyCafe> findByCafeId(Long cafeId);
    Optional<StudyCafe> findByUser_UserId(long userId);

    @Query("SELECT s FROM StudyCafe s WHERE :city IS NULL OR s.city = :city AND " +
            ":country IS NULL OR s.country = :country AND :town IS NULL OR s.town = :town")
    Page<StudyCafe> findStudyCafesByCityCountryTown(@Param("city") String city, @Param("country") String country,
                                                    @Param("town") String town, Pageable pageable);
}
