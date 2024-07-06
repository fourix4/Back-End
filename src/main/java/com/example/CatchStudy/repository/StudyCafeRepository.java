package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.StudyCafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyCafeRepository extends JpaRepository<StudyCafe, Long> {

    Optional<StudyCafe> findByCafeId(Long cafeId);

}
