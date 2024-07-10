package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.response.StudyCafeResponseDto;
import com.example.CatchStudy.domain.dto.response.StudyCafeSearchResponseDto;
import com.example.CatchStudy.domain.entity.StudyCafe;
import com.example.CatchStudy.global.enums.ImageType;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.CafeImageRepository;
import com.example.CatchStudy.repository.RoomRepository;
import com.example.CatchStudy.repository.SeatRepository;
import com.example.CatchStudy.repository.StudyCafeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudyCafeService {

    private final StudyCafeRepository studyCafeRepository;
    private final CafeImageRepository cafeImageRepository;
    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;

    public StudyCafeResponseDto getStudyCafe(long cafeId) {
        StudyCafe studyCafe = studyCafeRepository.findByCafeId(cafeId).orElseThrow(() -> new CatchStudyException(ErrorCode.STUDYCAFE_NOT_FOUND));
        int totalSeats = seatRepository.countSeatByStudyCafeId(cafeId);
        int availableSeats = seatRepository.countAvailableSeatsByStudyCafeId(cafeId);
        int totalRooms = roomRepository.countRoomByStudyCafeId(cafeId);
        int availableRooms = roomRepository.countAvailableRoomsByStudyCafeId(cafeId);
        List<String> cafeImageUrls = cafeImageRepository.findByStudyCafeUrlIdAndImageType(cafeId, ImageType.cafeImage);
        String seatingChartUrl = cafeImageRepository.findSeatingChartUrlByStudyCafeId(cafeId);

        return new StudyCafeResponseDto(studyCafe, cafeImageUrls, seatingChartUrl,
                totalSeats, availableSeats, totalRooms, availableRooms);
    }

    public Page<StudyCafeSearchResponseDto> searchStudyCafes(String city, String country, String town, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<StudyCafe> studyCafesPage = studyCafeRepository.findStudyCafesByCityCountryTown(city, country, town, pageable);

        return studyCafesPage.map(studyCafe -> new StudyCafeSearchResponseDto(
                studyCafe.getCafeId(),
                studyCafe.getCafeName(),
                studyCafe.getAddress(),
                cafeImageRepository.findThumbnailUrlByStudyCafeId(studyCafe.getCafeId())
        ));
    }
}
