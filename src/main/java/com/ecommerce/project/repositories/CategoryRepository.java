package com.ecommerce.project.repositories;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Id;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
