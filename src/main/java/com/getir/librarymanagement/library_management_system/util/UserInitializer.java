package com.getir.librarymanagement.library_management_system.util;

import com.getir.librarymanagement.library_management_system.enums.Role;
import com.getir.librarymanagement.library_management_system.model.entity.Book;
import com.getir.librarymanagement.library_management_system.model.entity.User;
import com.getir.librarymanagement.library_management_system.repository.BookRepository;
import com.getir.librarymanagement.library_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Initializes the system with default users and sample books
 * when the application starts. Useful for local testing and demos.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Executes initialization logic after the application context is loaded.
     */
    @Override
    public void run(String... args) {
        createDefaultLibrarian();
        createDefaultPatron();
        createSampleBooks();
    }

    /**
     * Creates a default librarian user if not already present in the database.
     * Useful for logging in to the admin interface initially.
     */
    private void createDefaultLibrarian() {
        String email = "librarian@getir.com";
        if (userRepository.findByEmail(email).isEmpty()) {
            User librarian = User.builder()
                    .name("Default Librarian")
                    .email(email)
                    .password(passwordEncoder.encode("password123"))
                    .phone("05000000000")
                    .role(Role.LIBRARIAN)
                    .registeredDate(LocalDateTime.now())
                    .build();
            userRepository.save(librarian);
            log.info("✅ Default LIBRARIAN user created: {} ---> password: password123", email);
        } else {
            log.info("ℹ️ LIBRARIAN user already exists: {} ---> password: password123", email);
        }
    }

    /**
     * Creates a default patron user for basic access testing.
     */
    private void createDefaultPatron() {
        String email = "patron@getir.com";
        if (userRepository.findByEmail(email).isEmpty()) {
            User patron = User.builder()
                    .name("Default Patron")
                    .email(email)
                    .password(passwordEncoder.encode("password456"))
                    .phone("05001112233")
                    .role(Role.PATRON)
                    .registeredDate(LocalDateTime.now())
                    .build();
            userRepository.save(patron);
            log.info("✅ Default PATRON user created: {} ---> password: password456", email);
        } else {
            log.info("ℹ️ PATRON user already exists: {} ---> password: password456", email);
        }
    }

    /**
     * Creates a set of sample books if none exist.
     * Useful for populating the initial catalog.
     */
    private void createSampleBooks() {
        if (bookRepository.findAll().isEmpty()) {
            Book book1 = Book.builder()
                    .title("Clean Code")
                    .author("Robert C. Martin")
                    .isbn("9780132350884")
                    .publicationDate(LocalDate.of(2008, 8, 1))
                    .genre("Programming")
                    .quantity(5)
                    .available(true)
                    .build();

            Book book2 = Book.builder()
                    .title("Effective Java")
                    .author("Joshua Bloch")
                    .isbn("9780134685991")
                    .publicationDate(LocalDate.of(2018, 1, 6))
                    .genre("Programming")
                    .quantity(1)
                    .available(true)
                    .build();

            bookRepository.saveAll(List.of(book1, book2));
            log.info("✅ Sample books created: Clean Code, Effective Java");
        } else {
            log.info("ℹ️ Sample books already exist.");
        }
    }
}
