package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    public Users findByUserName(String userName);
    public Users findByEmail(String email);
}
