package com.project.fondea.controller;

import com.project.fondea.dto.category.RegisterCategory;
import com.project.fondea.model.Category;
import com.project.fondea.service.CategoryService;
import com.project.fondea.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> create(@RequestBody RegisterCategory register) {
        var category = categoryService.register(register);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(category, "Categoria creada con exito"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> get() {
        var category = categoryService.getCategories();

        return ResponseEntity.ok(ApiResponse.ok(category, "Categorias obtenidas con exito"));
    }
}
