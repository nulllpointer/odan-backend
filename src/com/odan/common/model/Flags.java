package com.odan.common.model;

public abstract class Flags {


    public enum EntityStatus {
        INACTIVE, ACTIVE, DISABLED

    }


    public enum TaxRateType {
        AMOUNT, PERCENTAGE
    }

    public enum DiscountType {
        AMOUNT, PERCENTAGE
    }
    public enum OfferType {
        AMOUNT, PERCENTAGE
    }
    public enum SalesOfferType {
        AMOUNT, PERCENTAGE,RANGE
    }


    public enum SpecificDateType {
        TODAY, TOMORROW, YESTERDAY, LAST_MONTH, LAST_WEEK, NEXT_WEEK, NEXT_MONTH, THIS_WEEK, THIS_MONTH;
    }

    public enum ContactType {
        CUSTOMER, SUPPLIER
    }

    public enum TransactionType {
        CASH, ON_CREDIT

    }

    public enum Uom {
        ML, L, KG, GM, MUTHA, FULL, HALF, QUARTER, BOTTLE, FULL_1L, PLATE, HALF_PLATE, CUP, PIECE
    }

    public enum TransactionStatus {
        PAID, PARTIALLY_PAID, DUE
    }

    public enum ProductType {
        PURCHASE, SALE, BOTH
    }

    public enum TimePeriod {
        ONE_TIME, DAILY, WEEKLY, MONTHLY, QUARTERLY, HALF_YEARLY, YEARLY;

    }

    public enum EntityType {
        CONTACTS, PRODUCT, CATEGORY,CART_ITEM

    }

    public enum MainCategoryType {
        SNACKS, DRINKS, KHANA_KHAJA,ICE_CREAM

    }

}
