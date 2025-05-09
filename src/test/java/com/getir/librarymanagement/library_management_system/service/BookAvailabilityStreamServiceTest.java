package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.BookAvailabilityEvent;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

class BookAvailabilityStreamServiceTest {

    private final BookAvailabilityStreamService streamService = new BookAvailabilityStreamService();

    @Test
    void shouldEmitBookAvailabilityEvents() {

        Flux<BookAvailabilityEvent> flux = streamService.getAvailabilityStream();

        streamService.publishAvailabilityChange(1L, true);
        streamService.publishAvailabilityChange(2L, false);

        StepVerifier.create(flux)
                .expectNext(new BookAvailabilityEvent(1L, true))
                .expectNext(new BookAvailabilityEvent(2L, false))
                .thenCancel()
                .verify();
    }

    @Test
    void shouldNotEmitEventsWithoutPublishing() {
        Flux<BookAvailabilityEvent> flux = streamService.getAvailabilityStream();

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .thenCancel()
                .verify();
    }
}
