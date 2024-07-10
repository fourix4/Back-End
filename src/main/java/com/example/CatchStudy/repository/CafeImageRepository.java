package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.CafeImage;
import com.example.CatchStudy.global.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeImageRepository extends JpaRepository<CafeImage, Long> {
    CafeImage findByStudyCafeCafeIdAndImageType(Long cafeId, ImageType imageType);

}
