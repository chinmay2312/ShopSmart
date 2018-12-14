package com.chinmayg.hacks.shopsmart;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ShopItem {
	public int getID() {
		return ID;
	}
	
	private int ID;
    private float shopPrice;
    private int offerDaysRemaining;
    private String itemShopName;
    private String shopName;
	private int quantity;
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	private String imgUrl;
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
		
    public ShopItem(float shopPrice, int offerDaysRemaining, String itemShopName, String shopName, int quantity)	{
		this.shopName = shopName;
		//this.shopPrice = shopPrice;
		this.offerDaysRemaining = offerDaysRemaining;
		this.itemShopName = itemShopName;
		this.quantity = quantity;
		
		this.shopPrice = (float) Math.round(shopPrice*100f)/100f;
	}
	public ShopItem(float shopPrice, int offerDaysRemaining, String itemShopName, String shopName, int quantity, String imgUrl)	{
		this.shopName = shopName;
		this.imgUrl = imgUrl;
		this.offerDaysRemaining = offerDaysRemaining;
		this.itemShopName = itemShopName;
		this.quantity = quantity;
		this.shopPrice = (float) Math.round(shopPrice*100f)/100f;
	}
	public ShopItem(int ID, float shopPrice, int offerDaysRemaining, String itemShopName, String shopName, int quantity, String imgUrl)	{
		this.ID = ID;
		this.shopName = shopName;
		this.imgUrl = imgUrl;
		this.offerDaysRemaining = offerDaysRemaining;
		this.itemShopName = itemShopName;
		this.quantity = quantity;
		this.shopPrice = (float) Math.round(shopPrice*100f)/100f;
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
