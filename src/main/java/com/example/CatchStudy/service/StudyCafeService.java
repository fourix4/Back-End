package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.request.RoomsRequestDto;
import com.example.CatchStudy.domain.dto.request.StudyCafeRequestDto;
import com.example.CatchStudy.domain.dto.request.UsageFeeRequestDto;
import com.example.CatchStudy.domain.entity.*;
import com.example.CatchStudy.global.enums.ImageType;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudyCafeService {

    private final StudyCafeRepository studyCafeRepository;
    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final UsageFeeService usageFeeService;
    private final RoomService roomService;
    private final SeatService seatService;
    private final CafeImageService cafeImageService;

    @Transactional
    public void saveStudyCafe(StudyCafeRequestDto studyCafeRequestDto) {

        long userId = usersService.getCurrentUserId();
        Users user = usersRepository.findByUserId(userId).orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        LocalTime openingHours = LocalTime.parse(studyCafeRequestDto.getOpeningHours(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime closedHours = LocalTime.parse(studyCafeRequestDto.getOpeningHours(), DateTimeFormatter.ofPattern("HH:mm"));

        StudyCafe studyCafe = new StudyCafe(studyCafeRequestDto, openingHours, closedHours, user);
        StudyCafe savedStudyCafe = studyCafeRepository.save(studyCafe);

        //요금 정보 저장
        List<UsageFeeRequestDto> usageFees = studyCafeRequestDto.getUsageFee();
        for(UsageFeeRequestDto usageFeeRequestDto : usageFees) {
            usageFeeService.saveUsageFee(usageFeeRequestDto, savedStudyCafe);
        }

        // 룸 정보 저장
        List<RoomsRequestDto> rooms = studyCafeRequestDto.getRoomInfo().getRooms();
        long cancelAvailableTime = studyCafeRequestDto.getRoomInfo().getCancelAvailableTime();
        for(RoomsRequestDto roomsRequestDto : rooms) {
            roomService.saveRoom(roomsRequestDto, savedStudyCafe, cancelAvailableTime);
        }

        // 좌석 정보 저장
        for(int i = 1; i <= studyCafeRequestDto.getSeats(); i++) {
            seatService.saveSeat(Integer.toString(i), savedStudyCafe);
        }

        // 이미지 저장
        MultipartFile thumbnail = studyCafeRequestDto.getTitleImage();  // 썸네일
        MultipartFile seatChart = studyCafeRequestDto.getSeatChartImage();  // 좌석배치도
        List<MultipartFile> multipleImages = studyCafeRequestDto.getMultipleImages();   // 카페 이미지
        cafeImageService.saveCafeImages(thumbnail, seatChart, multipleImages, savedStudyCafe);
    }
}
