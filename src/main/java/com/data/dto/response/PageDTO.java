package com.data.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageDTO<T> {
    List<T> listDTO;
    int page;
    int totalPage;
    int size;
    int numElement;
    long totalElement;
    boolean first;
    boolean last;

    public static <T> PageDTO<T> of(Page<T> pageData) {
        PageDTO<T> dto = new PageDTO<>();
        dto.setListDTO(pageData.getContent());
        dto.setPage(pageData.getNumber());
        dto.setTotalPage(pageData.getTotalPages());
        dto.setSize(pageData.getSize());
        dto.setNumElement(pageData.getNumberOfElements());
        dto.setTotalElement(pageData.getTotalElements());
        dto.setFirst(pageData.isFirst());
        dto.setLast(pageData.isLast());
        return dto;
    }
}
