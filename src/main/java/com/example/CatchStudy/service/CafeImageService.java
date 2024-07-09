package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.entity.CafeImage;
import com.example.CatchStudy.domain.entity.StudyCafe;
import com.example.CatchStudy.global.enums.ImageType;
import com.example.CatchStudy.repository.CafeImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CafeImageService {

    private final CafeImageRepository cafeImageRepository;
    private final S3Service s3Service;

    public void saveCafeImages(MultipartFile thumbnail, MultipartFile seatChart, List<MultipartFile> multipleImages, StudyCafe studyCafe) {
        String thumbnailUrl = s3Service.upload(thumbnail);
        CafeImage cafeThumbnailImage = new CafeImage(ImageType.thumbnail, thumbnailUrl, studyCafe);
        cafeImageRepository.save(cafeThumbnailImage);

        String seatChartUrl = s3Service.upload(seatChart);
        CafeImage cafeSeatChartImage = new CafeImage(ImageType.seatingChart, seatChartUrl, studyCafe);
        cafeImageRepository.save(cafeSeatChartImage);

        for(MultipartFile imageFile : multipleImages) {
            String imageFileUrl = s3Service.upload(imageFile);
            CafeImage cafeImage = new CafeImage(ImageType.cafeImage, imageFileUrl, studyCafe);
            cafeImageRepository.save(cafeImage);
        }
    }
}
