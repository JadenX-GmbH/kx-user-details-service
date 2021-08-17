package com.jadenx.kxuserdetailsservice.repos;

import com.jadenx.kxuserdetailsservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUid(UUID uid);
}
