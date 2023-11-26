package role;
import util.*;

public class CrewMember implements Person{
    private Name name; 
    private String email;
    private String job;
    
    public CrewMember(Name name, String email,String job){
        this.name = name;
        this.email = email;
        this.job = job;
    }
    public CrewMember getCrewMember(){
        return this;
    }
    public void setJob(String job){
        this.job = job;
    }
    public String getJob(){
        return job;
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