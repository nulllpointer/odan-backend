package com.odan.billing.menu.category.model;


import com.odan.billing.menu.product.model.Product;
import com.odan.billing.menu.product.model.ProductDTO;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Entity
@Table(name = "category")
public class Category extends AbstractEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "principal_category_type")
    private Flags.PrincipalCategoryType principalCategoryType;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




    //JsonIgnore
    public List<ProductDTO> getProducts() {
        List<ProductDTO> dtos = null;
        List<Product> products = null;
        if (this.getId() != null) {
            products = (List<Product>) HibernateUtils.select("FROM Product WHERE category_id = " + this.getId());
        }
        return convertToDto(products);
    }

    @Transient
    private final Function<Product, ProductDTO> toDto = this::convertToDto;

    public List<ProductDTO> convertToDto(List<Product> products) {
        return products.stream()
                .map(toDto)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDto(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        // dto.setPrincipalCategoryType(product.getPrincipalCategoryType());
        dto.setProductType(product.getProductType());
        dto.setCategoryTitle(product.getCategory().getTitle());
        dto.setCategoryId(product.getCategory().getId());
        return dto;
    }

    public Flags.PrincipalCategoryType getPrincipalCategoryType() {
        return principalCategoryType;
    }

    public void setPrincipalCategoryType(Flags.PrincipalCategoryType principalCategoryType) {
        this.principalCategoryType = principalCategoryType;
    }
}
