package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.UsageFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsageFeeRepository extends JpaRepository<UsageFee, Long> {

    List<UsageFee> findAllByStudyCafe_CafeId(long cafeId);
    void deleteAllByStudyCafe_CafeId(long cafeId);
}
