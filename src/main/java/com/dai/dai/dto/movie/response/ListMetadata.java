package com.dai.dai.dto.movie.response;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ListMetadata {
    Integer totalPages;
    Integer pageSize;
    Integer currentPage;
}
