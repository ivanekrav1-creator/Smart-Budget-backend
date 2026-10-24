package ru.tbank.smart.budget.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.tbank.smart.budget.dao.category.entity.Category;
import ru.tbank.smart.budget.dao.category.repository.CategoryRepository;

@Service
public class CategoryService {

  private static final Long DEFAULT_CATEGORY_ID = 1L;

  private final Map<String, Long> mccToId;
  private final Map<String, Long> merchantToId;
  private final Map<Long, Category> idToCategory;

  public CategoryService(CategoryRepository categoryRepository) {
    List<Category> categories = categoryRepository.findAll();

    this.idToCategory = categories.stream().collect(Collectors.toMap(Category::getId, c -> c));

    this.mccToId =
        categories.stream()
            .filter(c -> c.getMccList() != null)
            .flatMap(
                c -> Arrays.stream(c.getMccList()).map(mcc -> Map.entry(mcc.trim(), c.getId())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));

    this.merchantToId =
        categories.stream()
            .filter(c -> c.getMerchantNameList() != null)
            .flatMap(
                c ->
                    Arrays.stream(c.getMerchantNameList())
                        .map(name -> Map.entry(name.trim(), c.getId())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
  }

  public Optional<Long> findIdByMcc(String mcc) {
    return Optional.ofNullable(mccToId.get(mcc.trim()));
  }

  public Optional<Long> findIdByMerchantName(String merchantName) {
    return Optional.ofNullable(merchantToId.get(merchantName.trim()));
  }

  public Optional<Category> findById(Long id) {
    return Optional.ofNullable(idToCategory.get(id));
  }

  public Optional<Category> getDefaultCategory() {
    return Optional.ofNullable(idToCategory.get(DEFAULT_CATEGORY_ID));
  }

  public List<Category> getAllCategories() {
    return idToCategory.values().stream().toList();
  }

  public boolean allIdsExist(List<Long> ids) {
    return idToCategory.keySet().containsAll(ids);
  }
}
