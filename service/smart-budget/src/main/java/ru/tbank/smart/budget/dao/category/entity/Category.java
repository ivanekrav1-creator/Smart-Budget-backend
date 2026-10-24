package ru.tbank.smart.budget.dao.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "icon")
  private String icon;

  @Column(name = "mcc_list", columnDefinition = "CHARACTER(4)[]")
  private String[] mccList;

  @Column(name = "merchant_name_list", columnDefinition = "VARCHAR(255)[]")
  private String[] merchantNameList;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getIcon() {
    return icon;
  }

  public String[] getMccList() {
    return mccList;
  }

  public String[] getMerchantNameList() {
    return merchantNameList;
  }
}
