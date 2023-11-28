package controller;

import util.*;

import java.util.ArrayList;

import database.Database;
import role.RegisteredCustomer;
import flightInfo.Ticket;
import flightInfo.Seat;

public class PaymentController {

    private RegisteredCustomer user;
    private Seat seat;
    private String departureTime;
    ArrayList<RegisteredCustomer> registeredCustomers = Database.getOnlyInstance().getRegisteredCustomerData();

    public PaymentController(String userEmail, Seat seat, String departureTime) {
        this.seat = seat;
        this.departureTime = departureTime;
        for (RegisteredCustomer customer : registeredCustomers) {
            if (customer.getEmail().equals(userEmail)) {
                this.user = customer;
            }
        }
    }

    public boolean paymentAvailable() {
        if (user.getPayment().getCreditCardNumber() != null && user.getPayment().getCVV() != 0) {
            return true;
        }
        return false;
    }

    public String getPaymentCardNum() {
        if (paymentAvailable()) {
            return user.getPayment().getCreditCardNumber();
        }
        return null;
    }

    public String getPaymentCVV() {
        if (paymentAvailable()) {
            return String.valueOf(user.getPayment().getCVV());
        }
        return null;
    }

    public void setPaymentInfo(String cardNum, String cvv) {
        if (cardNum.length() != 16 || cvv.length() != 3) {
            return;
        }
        else if (!paymentAvailable()){
            Payment newPayment = new Payment(cardNum, Integer.parseInt(cvv));
            System.out.println(newPayment.getCreditCardNumber());
            System.out.println(newPayment.getCVV());
            this.user.setPayment(newPayment);
        }
        System.out.println(user.getPayment().getCreditCardNumber());
        System.out.println(user.getPayment().getCVV());
        Database.getOnlyInstance().updateUser(user);
    }

    public void setStrat(String promoCode) {
        Payment payment = user.getPayment();
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

    public void setTicketPrice(double price, boolean insurance) {
        if (insurance) {
            price += 20;
        }
        Payment payment = user.getPayment();
        payment.setOwed(price);
    }

    public double getTicketPrice() {
        return user.getPayment().getAmountOwed();
    }

    public Ticket createTicket(boolean insurance) {
        Ticket ticket = new Ticket(seat.getSeatNum(), user.getPayment().getAmountOwed(), insurance, user.getName(), departureTime, seat.getSeatClass());
        return ticket;
    }
}
