package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.BookAvailabilityEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Reactive service for broadcasting real-time book availability changes using Project Reactor.
 */
@Service
public class BookAvailabilityStreamService {

    private final Sinks.Many<BookAvailabilityEvent> sink;

    /**
     * Initializes a multicast sink with backpressure buffer support.
     * Allows multiple subscribers to listen for availability events.
     */
    public BookAvailabilityStreamService() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    /**
     * Publishes a new book availability change event.
     *
     * @param bookId    ID of the book whose availability changed
     * @param available current availability status
     */
    public void publishAvailabilityChange(Long bookId, boolean available) {
        sink.tryEmitNext(new BookAvailabilityEvent(bookId, available));
    }

    /**
     * Returns a Flux stream that emits availability events to all subscribers.
     *
     * @return reactive stream of book availability events
     */
    public Flux<BookAvailabilityEvent> getAvailabilityStream() {
        return sink.asFlux();
    }
}
