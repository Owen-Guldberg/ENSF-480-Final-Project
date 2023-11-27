package util;

public class Payment {
    private String creditCardNum;
    private int CVV; 
    DiscountStrategy str;
    private double amountOwed;

    public Payment(String ccNum, int cvv){
        this.creditCardNum = ccNum; 
        this.CVV = cvv; 
        str = null;
        amountOwed = 0;
    }

    public String getCreditCardNumber(){
        return this.creditCardNum; 
    }

    public int getCVV(){
        return this.CVV; 
    }

    //no setters because if one changes they both woul change (ie getting a new card)
    public void updatePaymentInfo(String ccNum, int cvv){
        this.creditCardNum = ccNum; 
        this.CVV = cvv;
    }

    public Payment getPayment(){
        return this; 
    }
    public void setDiscount(DiscountStrategy str){
        this.str = str;
    }
    public void performDiscount(){
        amountOwed = str.applyDiscount(amountOwed);
    }
    public double getAmountOwed(){
        return amountOwed;
    }
    public void setOwed(double owed){
        amountOwed  = owed;
    }
}
