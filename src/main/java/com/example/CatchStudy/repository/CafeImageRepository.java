package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.CafeImage;
import com.example.CatchStudy.global.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CafeImageRepository extends JpaRepository<CafeImage, Long> {
    Optional<CafeImage> findByStudyCafeCafeIdAndImageType(Long cafeId, ImageType imageType);

    List<CafeImage> findAllByStudyCafe_CafeId(long cafeId);

    @Query("SELECT ci.cafeImage FROM CafeImage ci WHERE ci.studyCafe.cafeId = :cafeId AND ci.imageType = 'cafeImage'")
    List<String> findCafeImagesByStudyCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT ci.cafeImage FROM CafeImage ci WHERE ci.studyCafe.cafeId = :cafeId AND ci.imageType = 'seatingChart'")
    String findSeatingChartUrlByStudyCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT ci.cafeImage FROM CafeImage ci WHERE ci.studyCafe.cafeId = :cafeId AND ci.imageType = 'thumbnail'")
    String findThumbnailUrlByStudyCafeId(@Param("cafeId") Long cafeId);

    @Query("DELETE FROM CafeImage ci where ci.studyCafe.cafeId = :cafeId and  ci.imageType = 'thumbnail'")
    void deleteCafeThumbnailByCafeId(@Param("cafeId") Long cafeId);

    @Query("DELETE FROM CafeImage ci where ci.studyCafe.cafeId = :cafeId and  ci.imageType = 'cafeImage'")
    void deleteCafeImagesByCafeId(@Param("cafeId") Long cafeId);


}
