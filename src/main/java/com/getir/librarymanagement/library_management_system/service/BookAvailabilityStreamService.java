package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.BookAvailabilityEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class BookAvailabilityStreamService {

    private final Sinks.Many<BookAvailabilityEvent> sink;

    public BookAvailabilityStreamService() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publishAvailabilityChange(Long bookId, boolean available) {
        sink.tryEmitNext(new BookAvailabilityEvent(bookId, available));
    }

    public Flux<BookAvailabilityEvent> getAvailabilityStream() {
        return sink.asFlux();
    }
}
