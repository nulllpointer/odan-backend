package com.odan.billing.menu.category.model;


import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "category")
public class Category extends AbstractEntity {


    @Column(name = "title")
    private String title;

    @ManyToOne
    private Category parent;

    @Column(name = "description")
    private String description;

    private Flags.MainCategoryType type;

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

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

    public List<Category> getProducts() {
        List<Category> items = null;
        if(this.getId() != null) {
            items = (List<Category>) HibernateUtils.select("FROM Product WHERE category_id = " + this.getId());
        }

        return items;
    }

    public Flags.MainCategoryType getType() {
        return type;
    }

    public void setType(Flags.MainCategoryType type) {
        this.type = type;
    }
}
