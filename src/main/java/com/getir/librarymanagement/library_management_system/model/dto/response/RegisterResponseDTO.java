package com.getir.librarymanagement.library_management_system.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponseDTO {
    private Long id;
    private String email;
    private String role;
    private String token;
}
