package com.ecommerce.project.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {
    private Long productId;
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
}
