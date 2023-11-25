package role;
import util.*;

public class AirlineAgent implements Person{
    private Name name; 
    private String email;
    
    public AirlineAgent(Name name, String email){
        // add some
    }
    
    @Override
    public Name getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public void setName(Name name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public Name name() {
        return null;
    }
}