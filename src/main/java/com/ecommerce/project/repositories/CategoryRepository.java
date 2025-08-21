package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
