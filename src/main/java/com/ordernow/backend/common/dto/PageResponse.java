package com.ordernow.backend.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {
    private int totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private List<T> content;

    public static <T> PageResponse<T> createPageResponse(int totalElements, int currentPage, int size, List<T> content) {
        return PageResponse.<T>builder()
                .totalElements(totalElements)
                .totalPages((totalElements+size-1)/size)
                .currentPage(currentPage)
                .size(size)
                .content(content)
                .build();
    }
}
