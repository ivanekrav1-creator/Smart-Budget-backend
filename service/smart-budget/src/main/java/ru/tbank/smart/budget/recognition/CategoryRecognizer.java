package ru.tbank.smart.budget.recognition;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.tbank.smart.budget.service.CategoryService;

@Component
public class CategoryRecognizer {
  private final CategoryService categoryService;
  private final Logger log = LoggerFactory.getLogger(CategoryRecognizer.class);

  public CategoryRecognizer(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  public Long resolve(String mcc, String merchantName) {
    if (mcc != null) {
      Optional<Long> byMcc = resolveByMcc(mcc);
      if (byMcc.isPresent()) return byMcc.get();
    }
    if (merchantName != null) {
      Optional<Long> byMerchant = resolveByMerchant(merchantName);
      if (byMerchant.isPresent()) return byMerchant.get();
    }
    return defaultCategory();
  }

  private Optional<Long> resolveByMcc(String mcc) {
    return categoryService
        .findIdByMcc(mcc)
        .map(
            id -> {
              log.info("resolved by MCC: {} -> categoryId={}", mcc, id);
              return id;
            });
  }

  private Optional<Long> resolveByMerchant(String merchantName) {
    return categoryService
        .findIdByMerchantName(merchantName)
        .map(
            id -> {
              log.info("resolved by merchantName: {} -> categoryId={}", merchantName, id);
              return id;
            });
  }

  private Long defaultCategory() {
    return categoryService
        .getDefaultCategory()
        .map(
            category -> {
              log.warn("category not resolved, using default={}", category.getId());
              return category.getId();
            })
        .orElseThrow(() -> new IllegalStateException("Default category not found in cache"));
  }
}
