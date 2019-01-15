package com.sigit.taxcalculator.data;

import java.util.List;

/**
 * Created by Sigit Kurniawan on 1/14/2019.
 * Bill Response will be used to generate server response from Bill GET REQUEST
 */
public class BillResponse {

    private List<Bill> bill;
    private int priceSubTotal;
    private double taxSubTotal;
    private double grandTotal;

    public BillResponse(List<Bill> bills){
        bill = bills;

        for(Bill bill : bill){
            priceSubTotal += bill.getPrice();
            taxSubTotal += bill.getTax();
            grandTotal += bill.getAmount();
        }
    }

    public List<Bill> getBill() {
        return bill;
    }

    public int getPriceSubTotal() {
        return priceSubTotal;
    }

    public double getTaxSubTotal() {
        return taxSubTotal;
    }

    public double getGrandTotal() {
        return grandTotal;
    }
}
