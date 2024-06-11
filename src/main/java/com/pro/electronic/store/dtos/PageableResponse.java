package com.pro.electronic.store.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//used generic class to easliy change the object type dto or user
public class PageableResponse <T>{

    private List<T> content;
    private  int PageNumber;
    private  int PageSize;
    private  long  totalElements;
    private  int totalPages;
    private boolean lastPage;


}
