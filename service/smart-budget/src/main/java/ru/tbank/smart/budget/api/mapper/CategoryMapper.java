package ru.tbank.smart.budget.api.mapper;

import org.springframework.stereotype.Component;
import ru.tbank.smart.budget.dao.category.entity.Category;
import ru.tbank.smart.budget.model.CategoryDto;

@Component
public class CategoryMapper {
  public CategoryDto toDto(Category category) {
    return new CategoryDto(category.getId(), category.getName(), category.getIcon());
  }
}
