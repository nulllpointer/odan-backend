package com.odan.billing.menu.product.model;

import com.odan.billing.menu.category.model.Category;
import com.odan.common.application.CommandException;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.ProductType;


public class ProductDTO {

    private  Long id;

    private String title;

    private String categoryTitle;

    private String description;

    private ProductType productType;

  //  private Flags.PrincipalCategoryType principalCategoryType;

    private  Long categoryId;

    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

  /*  public Flags.PrincipalCategoryType getPrincipalCategoryType() {
        return principalCategoryType;
    }

    public void setPrincipalCategoryType(Flags.PrincipalCategoryType principalCategoryType) {
        this.principalCategoryType = principalCategoryType;
    }
*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
