package org.csu.javagateway.dto;

import lombok.Data;

@Data
public class BorrowRequestDTO {
    private String personnelId;
    private String materialId;
}
