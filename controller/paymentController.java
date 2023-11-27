package controller;

import util.*;

import java.util.ArrayList;


public class PaymentController {

    public PaymentController() {}

    public void setStrat(String promoCode, Payment payment) {
        if ("MEMBER15".equals(promoCode)) {
            fifteenDiscount disc = new fifteenDiscount();
            double discountedPrice = disc.applyDiscount(payment.getAmountOwed());
            payment.setOwed(discountedPrice);
        }
        else if("MEMBER50".equals(promoCode)){
            fiftyDiscount disc = new fiftyDiscount();
            double discountedPrice = disc.applyDiscount(payment.getAmountOwed());
            payment.setOwed(discountedPrice);
        }
    }

    public void setTicketPrice(double price, Payment userPayment) {
        userPayment.setOwed(price);
    }
}