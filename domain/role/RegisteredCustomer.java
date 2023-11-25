package role;
import util.*;

public class RegisteredCustomer implements Person{
    private Name name; 
    private String email;
    private String password;
    
    public RegisteredCustomer(Name name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
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
}