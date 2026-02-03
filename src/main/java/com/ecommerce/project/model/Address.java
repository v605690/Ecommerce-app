package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street must be at least 5 characters long")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be at least 5 characters long")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "City name must be at least 4 characters long")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must be at least 2 characters long")
    private String state;

    @NotBlank
    @Size(min = 6, message = "ZipCode name must be at least 6 characters long")
    private String zipCode;

    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters long")
    private String country;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

}
