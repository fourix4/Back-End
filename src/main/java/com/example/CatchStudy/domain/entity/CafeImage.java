package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.global.enums.ImageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "cafe_image")
public class CafeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cafeImageId;

    @Enumerated
    private Enum<ImageType> imageType;

    @Column
    private String cafeImage;
}
