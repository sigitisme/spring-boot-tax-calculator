package com.sigit.taxcalculator.data;

import com.sigit.taxcalculator.entity.Tax;

/**
 * Created by Sigit Kurniawan on 1/14/2019.
 * Bill is used for type, refundable, tax and amount calculation
 */

public class Bill {

    private String name;
    private int taxCode;
    private String type;
    private String refundable;
    private int price;
    private double tax;
    private double amount;

    public Bill(Tax tax){

        name = tax.getName();
        taxCode = tax.getTaxCode();
        price = tax.getPrice();

        switch (tax.getTaxCode()){
            //taxCode is set default to 1 if other number (not 1,2,3) is inserted
            case 1:
            default:
                type = "Food & Beverage";
                refundable = "Yes";

                //tax is 10% of price
                this.tax = (0.1 * price);
                break;
            case 2:
                type = "Tobacco";
                refundable = "No";

                //tax is 10 + (2% of price)
                this.tax = 10 + (0.02 * price);
                break;
            case 3:
                type = "Entertainment";
                refundable = "No";

                //if price < 100, tax is 0
                //if price >= 100, tax is 1% of (price - 100)
                this.tax = (price < 100) ? 0 : (0.01 * (price - 100));
                break;
        }

        //set total amount
        amount = price + this.tax;
    }

    public String getType() {
        return type;
    }

    public String getRefundable() {
        return refundable;
    }

    public double getTax() {
        return tax;
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public int getTaxCode() {
        return taxCode;
    }

    public int getPrice() {
        return price;
    }
}
