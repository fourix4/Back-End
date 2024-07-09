package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.UsageFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsageFeeRepository extends JpaRepository<UsageFee, Long> {
}
