package com.chinmayg.hacks.shopsmart;

public class ShopItem {
    private float shopPrice;
    private int offerDaysRemaining;
    private String itemShopName;
    private String shopName;

    public ShopItem(float shopPrice, int offerDaysRemaining, String itemShopName, String shopName) {
        this.shopName = shopName;
        this.shopPrice = shopPrice;
        this.offerDaysRemaining = offerDaysRemaining;
        this.itemShopName = itemShopName;
    }

    public float getShopPrice() {
        return shopPrice;
    }

    public int getOfferDaysRemaining() {
        return offerDaysRemaining;
    }

    public String getItemShopName() {
        return itemShopName;
    }

    public String getShopName() {
        return shopName;
    }
}
