package com.getir.librarymanagement.library_management_system.util;

import com.getir.librarymanagement.library_management_system.model.dto.request.BookSearchRequestDTO;
import com.getir.librarymanagement.library_management_system.model.entity.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic JPA Specification builder for filtering Book entities
 * based on optional search parameters such as title, author, ISBN, and genre.
 */
public class BookSpecification {

    /**
     * Creates a dynamic Specification<Book> based on non-null and non-blank fields in the request DTO.
     * Useful for constructing flexible, multi-criteria search queries.
     *
     * @param dto the search filters sent by client
     * @return JPA Specification that can be used with Spring Data repositories
     */
    public static Specification<Book> withFilters(BookSearchRequestDTO dto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add a filter for title if provided
            if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("title")), "%" + dto.getTitle().toLowerCase() + "%")
                );
            }

            // Add a filter for author if provided
            if (dto.getAuthor() != null && !dto.getAuthor().isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("author")), "%" + dto.getAuthor().toLowerCase() + "%")
                );
            }

            // Add a filter for ISBN if provided
            if (dto.getIsbn() != null && !dto.getIsbn().isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("isbn")), "%" + dto.getIsbn().toLowerCase() + "%")
                );
            }

            // Add a filter for genre if provided
            if (dto.getGenre() != null && !dto.getGenre().isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("genre")), "%" + dto.getGenre().toLowerCase() + "%")
                );
            }

            // Combine all predicates with logical AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
