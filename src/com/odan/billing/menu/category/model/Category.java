package com.odan.billing.menu.category.model;


import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class Category extends AbstractEntity {


    @Column(name = "title")
    private String title;

    @ManyToOne
    private Category parent;

    @Column(name = "description")
    private String description;

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


}
