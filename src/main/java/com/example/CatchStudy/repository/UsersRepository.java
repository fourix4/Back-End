package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserId(Long userId);
    public Users findByUserName(String userName);
    public Users findByEmail(String email);


}
