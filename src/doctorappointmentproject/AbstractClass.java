package doctorappointmentproject;

public abstract class AbstractClass {
    public String name ;
    public String gender;
    public String phoneNumber;
    
    public AbstractClass(String name , String gender , String phoneNumber){
        this.name        = name;
        this.gender      = gender;
        this.phoneNumber = phoneNumber;
    }
}
