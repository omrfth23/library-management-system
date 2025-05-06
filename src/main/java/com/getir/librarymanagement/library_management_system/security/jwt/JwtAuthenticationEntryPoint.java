package com.getir.librarymanagement.library_management_system.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom entry point for handling unauthorized access attempts in JWT-based security.
 * This class intercepts authentication errors and returns a structured JSON response
 * instead of the default Spring Security error page.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Triggered whenever an unauthenticated user tries to access a secured resource.
     * Responds with HTTP 401 Unauthorized and a JSON body describing the error.
     *
     * @param request       the HttpServletRequest
     * @param response      the HttpServletResponse
     * @param authException the exception that caused the authentication to fail
     * @throws IOException  if writing the JSON response fails
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        final int status = HttpStatus.UNAUTHORIZED.value();

        // Build a standardized error response body
        JwtAuthErrorResponse error = new JwtAuthErrorResponse(
                status,
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Authentication required",
                request.getRequestURI()
        );

        response.setStatus(status);
        response.setContentType("application/json");
        OBJECT_MAPPER.writeValue(response.getWriter(), error);
    }

    /**
     * Structure of the JSON response sent when authentication fails.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class JwtAuthErrorResponse {
        private int status;
        private String error;
        private String message;
        private String path;
    }
}
