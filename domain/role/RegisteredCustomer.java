package role;

import util.*;
import database.*;

public class RegisteredCustomer implements Person{
    private Name name; 
    private String email;
    private String password;
    private Address address; 
    private Payment payment; 
    
    public RegisteredCustomer(Name name, String email, String password, Address a, Payment p){
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = a;
        this.payment = p; 
    }
    
    @Override
    public Name getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setName(Name name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Name name() {
        return null;
    }

     public Payment getPayment(){
        return this.payment; 
    }

    public void setPayment(Payment p){
        this.payment = p; 
    }

    public Address getAddress(){
        return this.address; 
    }

    public void setAddress(Address a){
        this.address = a; 
    }
}
