package com.project.fondea.service;

import com.project.fondea.dto.category.RegisterCategory;
import com.project.fondea.model.Category;
import com.project.fondea.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category register(RegisterCategory registerCategory) {

        var category = Category.builder()
                .name(registerCategory.getName()).build();

        return categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
