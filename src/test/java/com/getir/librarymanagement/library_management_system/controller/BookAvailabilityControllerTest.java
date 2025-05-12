package com.getir.librarymanagement.library_management_system.controller;

import com.getir.librarymanagement.library_management_system.model.dto.BookAvailabilityEvent;
import com.getir.librarymanagement.library_management_system.service.BookAvailabilityStreamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static org.mockito.Mockito.when;

@WebFluxTest(BookAvailabilityController.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BookAvailabilityControllerTest {

    @MockBean
    private BookAvailabilityStreamService streamService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(new BookAvailabilityController(streamService)).build();
    }

    @Test
    void testStreamAvailability_withData_shouldReturnStream() {
        // Mocked data
        BookAvailabilityEvent event1 = new BookAvailabilityEvent(1L, true);
        BookAvailabilityEvent event2 = new BookAvailabilityEvent(2L, false);

        // Flux with delay to simulate a stream
        Flux<BookAvailabilityEvent> fluxStream = Flux.just(event1, event2).delayElements(Duration.ofMillis(100));

        when(streamService.getAvailabilityStream()).thenReturn(fluxStream);

        webTestClient.get()
                .uri("/api/stream/books/availability")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .returnResult(BookAvailabilityEvent.class)
                .getResponseBody()
                .collectList()
                .doOnNext(events -> {
                    assert events.size() == 2;
                    assert events.get(0).getBookId().equals(1L);
                    assert events.get(0).isAvailable();
                    assert events.get(1).getBookId().equals(2L);
                    assert !events.get(1).isAvailable();
                })
                .block();
    }

    @Test
    void testStreamAvailability_noData_shouldReturnEmptyStream() {
        // Empty Flux
        Flux<BookAvailabilityEvent> emptyFlux = Flux.empty();

        when(streamService.getAvailabilityStream()).thenReturn(emptyFlux);

        webTestClient.get()
                .uri("/api/stream/books/availability")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBodyList(BookAvailabilityEvent.class)
                .hasSize(0);
    }
}
