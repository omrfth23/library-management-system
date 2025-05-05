package com.getir.librarymanagement.library_management_system.util;

import com.getir.librarymanagement.library_management_system.model.dto.request.BookSearchRequestDTO;
import com.getir.librarymanagement.library_management_system.model.entity.Book;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> withFilters(BookSearchRequestDTO dto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + dto.getTitle().toLowerCase() + "%"));
            }
            if (dto.getAuthor() != null && !dto.getAuthor().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("author")), "%" + dto.getAuthor().toLowerCase() + "%"));
            }
            if (dto.getIsbn() != null && !dto.getIsbn().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("isbn")), "%" + dto.getIsbn().toLowerCase() + "%"));
            }
            if (dto.getGenre() != null && !dto.getGenre().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("genre")), "%" + dto.getGenre().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
