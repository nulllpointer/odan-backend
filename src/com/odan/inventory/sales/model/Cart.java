package com.odan.inventory.sales.model;

import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name="cart")
public class Cart extends AbstractEntity{

    @Column(name="identifier")
    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
