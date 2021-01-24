package com.rao.mongodb.demo.dao;

/**
 * @author raoshihong
 * @date 2021-01-17 23:51
 */
public class Item {

    private String sku;

    private String qty;

    private String price;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
