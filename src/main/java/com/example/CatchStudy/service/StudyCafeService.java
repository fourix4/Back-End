package com.example.CatchStudy.service;

import com.example.CatchStudy.repository.StudyCafeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudyCafeService {

    private final StudyCafeRepository studyCafeRepository;
}
