package com.ecommerce.project.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category {
    private Long categoryId;
    private String categoryName;
}
