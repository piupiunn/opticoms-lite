package de.opticoms.nms.lite.controller.model;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedData <T> {
    public long totalElements;
    public int totalPages;
    public int numberOfElements;
    public Collection<T> content;
}
