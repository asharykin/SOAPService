package ru.codemark.soap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.codemark.soap.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
