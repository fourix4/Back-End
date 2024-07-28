package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.request.RoomsRequestDto;
import com.example.CatchStudy.domain.dto.request.ManagerRequestDto;
import com.example.CatchStudy.domain.dto.request.UsageFeeRequestDto;
import com.example.CatchStudy.domain.dto.response.ManagerResponseDto;
import com.example.CatchStudy.domain.dto.response.RoomInfoResponseDto;
import com.example.CatchStudy.domain.dto.response.UsageFeeResponseDto;
import com.example.CatchStudy.domain.entity.*;
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
public class ManagerService {

    private final StudyCafeRepository studyCafeRepository;
    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final UsageFeeService usageFeeService;
    private final RoomService roomService;
    private final SeatService seatService;
    private final CafeImageService cafeImageService;
    private final CafeImageRepository cafeImageRepository;

    @Transactional
    public void saveStudyCafe(ManagerRequestDto managerRequestDto, MultipartFile thumbnail, List<MultipartFile> multipleImages) {

        long userId = usersService.getCurrentUserId();
        Users user = usersRepository.findByUserId(userId).orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        LocalTime openingHours = LocalTime.parse(managerRequestDto.getOpeningHours(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime closedHours = LocalTime.parse(managerRequestDto.getOpeningHours(), DateTimeFormatter.ofPattern("HH:mm"));

        StudyCafe studyCafe = new StudyCafe(managerRequestDto, openingHours, closedHours, user);
        StudyCafe savedStudyCafe = studyCafeRepository.save(studyCafe);

        //요금 정보 저장
        List<UsageFeeRequestDto> usageFees = managerRequestDto.getUsageFee();
        for(UsageFeeRequestDto usageFeeRequestDto : usageFees) {
            usageFeeService.saveUsageFee(usageFeeRequestDto, savedStudyCafe);
        }

        // 룸 정보 저장
        List<RoomsRequestDto> rooms = managerRequestDto.getRoomInfo().getRooms();
        long cancelAvailableTime = managerRequestDto.getRoomInfo().getCancelAvailableTime();
        for(RoomsRequestDto roomsRequestDto : rooms) {
            roomService.saveRoom(roomsRequestDto, savedStudyCafe, cancelAvailableTime);
        }

        // 좌석 정보 저장
        for(int i = 1; i <= managerRequestDto.getSeats(); i++) {
            seatService.saveSeat(Integer.toString(i), savedStudyCafe);
        }

        // 이미지 저장
        cafeImageService.saveCafeImages(thumbnail, multipleImages, savedStudyCafe);
    }

    @Transactional
    public void updateStudyCafe(ManagerRequestDto managerRequestDto, MultipartFile thumbnail, List<MultipartFile> multipleImages) {
        long userId = usersService.getCurrentUserId();
        LocalTime openingHours = LocalTime.parse(managerRequestDto.getOpeningHours(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime closedHours = LocalTime.parse(managerRequestDto.getOpeningHours(), DateTimeFormatter.ofPattern("HH:mm"));
        StudyCafe studyCafe = studyCafeRepository.findByUser_UserId(userId).orElseThrow(() -> new CatchStudyException(ErrorCode.USER_NOT_FOUND));
        studyCafe.update(managerRequestDto, openingHours, closedHours);

        usageFeeService.deleteUsageFee(studyCafe.getCafeId());
        List<UsageFeeRequestDto> usageFees = managerRequestDto.getUsageFee();
        for(UsageFeeRequestDto usageFeeRequestDto : usageFees) {
            usageFeeService.saveUsageFee(usageFeeRequestDto, studyCafe);
        }

        roomService.deleteRoom(studyCafe.getCafeId());
        List<RoomsRequestDto> rooms = managerRequestDto.getRoomInfo().getRooms();
        long cancelAvailableTime = managerRequestDto.getRoomInfo().getCancelAvailableTime();
        for(RoomsRequestDto roomsRequestDto : rooms) {
            roomService.saveRoom(roomsRequestDto, studyCafe, cancelAvailableTime);
        }

        seatService.deleteSeat(studyCafe.getCafeId());
        for(int i = 1; i <= managerRequestDto.getSeats(); i++) {
            seatService.saveSeat(Integer.toString(i), studyCafe);
        }

        cafeImageService.deleteCafeImages(thumbnail, multipleImages, studyCafe);
        cafeImageService.saveCafeImages(thumbnail, multipleImages, studyCafe);
    }

    public ManagerResponseDto getStudyCafe() {
        long userId = usersService.getCurrentUserId();
        StudyCafe studyCafe = studyCafeRepository.findByUser_UserId(userId).orElseThrow(() -> new CatchStudyException(ErrorCode.STUDYCAFE_NOT_FOUND));
        int seats = seatService.getSeatsCount(studyCafe.getCafeId());
        RoomInfoResponseDto roomInfoResponseDto = roomService.getRoomInfoResponseDto(studyCafe.getCafeId());
        List<UsageFeeResponseDto> usageFeeResponseDtoList = usageFeeService.getUsageFeeResponseDto(studyCafe.getCafeId());
        String thumbnailUrl = cafeImageRepository.findThumbnailUrlByStudyCafeId(studyCafe.getCafeId());
        String seatingChartUrl = cafeImageRepository.findSeatingChartUrlByStudyCafeId(studyCafe.getCafeId());
        List<String> cafeImageUrls = cafeImageRepository.findCafeImagesByStudyCafeId(studyCafe.getCafeId());

        return new ManagerResponseDto(studyCafe, seats, roomInfoResponseDto, usageFeeResponseDtoList, thumbnailUrl, cafeImageUrls, seatingChartUrl);
    }
}
