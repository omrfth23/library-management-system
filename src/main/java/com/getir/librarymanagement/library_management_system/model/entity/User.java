package com.getir.librarymanagement.library_management_system.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.getir.librarymanagement.library_management_system.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a user in the system, either a LIBRARIAN or a PATRON.
 */
@Entity
@Table(name = "users") // 'user' is a reserved keyword in PostgreSQL
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Phone cannot be blank")
    @Column(unique = true, nullable = false)
    private String phone;

    /**
     * Role of the user (e.g., LIBRARIAN or PATRON).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime registeredDate;

    /**
     * One-to-many relationship with borrow records.
     * This field represents the list of books borrowed by the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Prevents circular references in JSON output
    private List<BorrowRecord> borrowRecords;
}