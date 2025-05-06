package com.getir.librarymanagement.library_management_system.controller;

import com.getir.librarymanagement.library_management_system.model.dto.BookAvailabilityEvent;
import com.getir.librarymanagement.library_management_system.service.BookAvailabilityStreamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * REST controller for streaming real-time book availability updates.
 * Uses Server-Sent Events (SSE) to push updates to clients.
 */
@RestController
@RequestMapping("/api/stream/books")
@RequiredArgsConstructor
public class BookAvailabilityController {

    private final BookAvailabilityStreamService streamService;

    /**
     * Streams book availability events to subscribed clients using SSE.
     *
     * @return a Flux that emits BookAvailabilityEvent objects
     */
    @Operation(
            summary = "Stream book availability changes",
            description = "Streams real-time updates of book availability using Server-Sent Events (SSE). "
                    + "Clients can subscribe to receive availability change events as they happen.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Streaming started successfully"),
            }
    )
    @GetMapping(value = "/availability", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BookAvailabilityEvent> streamAvailability() {
        return streamService.getAvailabilityStream();
    }
}
