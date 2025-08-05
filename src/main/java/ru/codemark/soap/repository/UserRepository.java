package ru.codemark.soap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.codemark.soap.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);
}