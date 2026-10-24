package ru.tbank.smart.budget.dao.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.smart.budget.dao.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
