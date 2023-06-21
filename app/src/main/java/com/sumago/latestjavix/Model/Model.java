package com.sumago.latestjavix.Model;

public class Model {
    private String sNo;
    private String product;
    private String category;
    private String price;
    private String premeal;
    private String medicine;

    public Model(String sNo, String product, String category, String price,String premeal,String medicine) {
        this.sNo = sNo;
        this.product = product;
        this.category = category;
        this.price = price;
        this.premeal = premeal;
        this.medicine = medicine;
    }

    public String getsNo() {
        return sNo;
    }

    public String getProduct() {
        return product;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }
    public String getPremeal() {
        return premeal;
    }
    public String getMedicine() {
        return medicine;
    }
}
