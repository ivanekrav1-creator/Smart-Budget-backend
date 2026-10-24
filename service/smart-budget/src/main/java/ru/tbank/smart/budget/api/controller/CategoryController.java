package ru.tbank.smart.budget.api.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.smart.budget.api.CategoriesApi;
import ru.tbank.smart.budget.api.mapper.CategoryMapper;
import ru.tbank.smart.budget.model.CategoryDto;
import ru.tbank.smart.budget.service.CategoryService;

@RestController
public class CategoryController implements CategoriesApi {
  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;

  public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
    this.categoryService = categoryService;
    this.categoryMapper = categoryMapper;
  }

  @Override
  public ResponseEntity<List<CategoryDto>> getCategories() {
    return ResponseEntity.ok(
        categoryService.getAllCategories().stream().map(categoryMapper::toDto).toList());
  }
}
