package flightInfo;
import java.util.ArrayList;
import role.*;


public class Airline {
    private String companyName;
    private ArrayList<Aircraft> aircrafts;
    private AirlineAgent agent;
    private ArrayList<CrewMember> crewMembers;

    // Constructor
    public Airline(String companyName, ArrayList<Aircraft> aircrafts, AirlineAgent agent, ArrayList<CrewMember> crewMembers) {
        this.companyName = companyName;
        this.aircrafts = aircrafts;
        this.agent = agent;
        this.crewMembers = crewMembers;
    }

    // Getters
    public String getCompanyName() {
        return companyName;
    }

    public ArrayList<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public AirlineAgent getAgent() {
        return agent;
    }

    public ArrayList<CrewMember> getCrewMembers() {
        return crewMembers;
    }

    // Setters
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setAircrafts(ArrayList<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public void setAgent(AirlineAgent agent) {
        this.agent = agent;
    }
    // set an entire crew member list
    public void setCrewMembers(ArrayList<CrewMember> crewMembers) {
        this.crewMembers = crewMembers;
    }
    // add a single crew member
    public void addCrewMember(CrewMember crewMember){
        this.crewMembers.add(crewMember);
    }
    // remove a single crew member
    public void removeCrewMember(CrewMember crewMember){
        this.crewMembers.remove(crewMember);
    }
}