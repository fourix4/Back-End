package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.entity.CafeImage;
import com.example.CatchStudy.domain.entity.StudyCafe;
import com.example.CatchStudy.global.enums.ImageType;
import com.example.CatchStudy.repository.CafeImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CafeImageService {

    private final CafeImageRepository cafeImageRepository;
    private final S3Service s3Service;

    public void saveCafeThumbnail(MultipartFile thumbnail, StudyCafe studyCafe) {
        if(thumbnail != null) {
            String thumbnailUrl = s3Service.upload(thumbnail);
            CafeImage cafeThumbnailImage = new CafeImage(ImageType.thumbnail, thumbnailUrl, studyCafe);
            cafeImageRepository.save(cafeThumbnailImage);
        }
    }

    public void saveCafeImages(List<MultipartFile> multipleImages, StudyCafe studyCafe) {
        if(multipleImages != null) {
            for(MultipartFile imageFile : multipleImages) {
                String imageFileUrl = s3Service.upload(imageFile);
                CafeImage cafeImage = new CafeImage(ImageType.cafeImage, imageFileUrl, studyCafe);
                cafeImageRepository.save(cafeImage);
            }
        }
    }

    public void deleteCafeImages(long cafeId, ImageType type) {
        if(type.equals(ImageType.thumbnail)) {
            String thumbnailUrl = cafeImageRepository.findThumbnailUrlByStudyCafeId(cafeId);
            s3Service.deleteImageFromS3(thumbnailUrl);
        } else if(type.equals(ImageType.cafeImage)){
            List<String> cafeImageUrls = cafeImageRepository.findCafeImagesByStudyCafeId(cafeId);
            for(String url : cafeImageUrls) s3Service.deleteImageFromS3(url);
        }
    }
}
