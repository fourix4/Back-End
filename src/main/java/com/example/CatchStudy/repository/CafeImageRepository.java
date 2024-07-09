package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.CafeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeImageRepository extends JpaRepository<CafeImage, Long> {

    List<CafeImage> findAllByStudyCafe_CafeId(long cafeId);
}
