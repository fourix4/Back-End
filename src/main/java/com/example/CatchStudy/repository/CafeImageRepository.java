package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.CafeImage;
import com.example.CatchStudy.global.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CafeImageRepository extends JpaRepository<CafeImage, Long> {
    CafeImage findByStudyCafeCafeIdAndImageType(Long cafeId, ImageType imageType);

    List<CafeImage> findAllByStudyCafe_CafeId(long cafeId);

    @Query("SELECT ci.cafeImage FROM CafeImage ci WHERE ci.studyCafe.cafeId = :cafeId AND ci.imageType = :imageType")
    List<String> findByStudyCafeUrlIdAndImageType(@Param("cafeId") Long cafeId, @Param("imageType") ImageType imageType);

    @Query("SELECT ci.cafeImage FROM CafeImage ci WHERE ci.studyCafe.cafeId = :cafeId AND ci.imageType = 'seatingChart'")
    String findSeatingChartUrlByStudyCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT ci.cafeImage FROM CafeImage ci WHERE ci.studyCafe.cafeId = :cafeId AND ci.imageType = 'thumbnail'")
    String findThumbnailUrlByStudyCafeId(@Param("cafeId") Long cafeId);

}
