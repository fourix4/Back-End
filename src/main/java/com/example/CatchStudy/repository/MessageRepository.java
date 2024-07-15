package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
