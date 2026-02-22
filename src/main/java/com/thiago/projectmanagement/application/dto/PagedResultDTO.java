package com.thiago.projectmanagement.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResultDTO<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;

    public static <T> PagedResultDTO<T> of(List<T> content, long totalElements, int totalPages, int number, int size) {
        return new PagedResultDTO<>(content, totalElements, totalPages, number, size);
    }
}
