package com.getir.librarymanagement.library_management_system.controller;

import com.getir.librarymanagement.library_management_system.model.dto.BookAvailabilityEvent;
import com.getir.librarymanagement.library_management_system.service.BookAvailabilityStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/stream/books")
@RequiredArgsConstructor
public class BookAvailabilityController {

    private final BookAvailabilityStreamService streamService;

    @GetMapping(value = "/availability", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BookAvailabilityEvent> streamAvailability() {
        return streamService.getAvailabilityStream();
    }
}
