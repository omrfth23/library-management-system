package com.getir.librarymanagement.library_management_system.repository;

import com.getir.librarymanagement.library_management_system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides basic CRUD operations and user-specific queries.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     * Commonly used for authentication.
     *
     * @param email user's email
     * @return optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given email exists.
     * Useful for registration validation.
     *
     * @param email user's email
     * @return true if user exists
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a user with the given phone number exists.
     * Useful for phone uniqueness validation.
     *
     * @param phone user's phone number
     * @return true if phone number is already used
     */
    boolean existsByPhone(String phone);
}
