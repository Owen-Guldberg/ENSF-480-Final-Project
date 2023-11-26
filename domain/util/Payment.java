package util;

public class Payment {
    private String creditCardNum;
    private int CVV; 

    public Payment(String ccNum, int cvv){
        this.creditCardNum = ccNum; 
        this.CVV = cvv; 
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
}
