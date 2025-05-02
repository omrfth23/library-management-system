package com.getir.librarymanagement.library_management_system.model.dto.response;

import com.getir.librarymanagement.library_management_system.enums.Role;
import com.getir.librarymanagement.library_management_system.model.entity.BorrowRecord;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserResponseDTO {

    private Long userId;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private LocalDateTime registeredDate;
    List<BorrowRecord> borrowRecords;
}