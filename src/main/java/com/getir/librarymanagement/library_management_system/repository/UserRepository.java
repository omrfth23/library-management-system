package com.getir.librarymanagement.library_management_system.repository;

import com.getir.librarymanagement.library_management_system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); //Authentication için gerekli
    boolean existsByEmail(String email); //Kullanıcı kayıt validation'u için
    boolean existsByPhone(String phone);
}
