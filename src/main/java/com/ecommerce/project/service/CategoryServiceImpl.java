package com.ecommerce.project.service;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

   // private final List<Category> categories = new ArrayList<>();
   // private Long nextId = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new APIException("Category does not exist");
        }
        // Using DTO
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    @Override
      // Takes a CategoryDTO as input and returns a CategoryDTO representing the newly created category.
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Uses ModelMapper to convert the incoming CategoryDTO into a Category entity
        Category category = modelMapper.map(categoryDTO, Category.class);
        // Checks if a category with the same name already exist in DB, if duplicate found, throws a exception
       Category categoryFromDB = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
       if (categoryFromDB != null) {
           throw new APIException("Category with the name " + categoryDTO.getCategoryName() + " already exists !!!");
       }
       // Saves the new category to the database using JPA save() method
       Category savedCategory = categoryRepository.save(category);
       // Converts the saved entity back to a DTO for the response
        // ensure the returned object includes auto-gen ID and other DB fields
       return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public String deleteCategory(Long categoryId) {

       Category category = categoryRepository.findById(categoryId)
               .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        categoryRepository.delete(category);
        return "Category with categoryId: " + categoryId + "deleted successfully !!";
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() ->  new ResourceNotFoundException("Category", "categoryId", categoryId));

        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryDTO.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
