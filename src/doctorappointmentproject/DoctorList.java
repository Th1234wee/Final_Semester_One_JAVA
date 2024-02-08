/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doctorappointmentproject;
import java.sql.*;

/**
 *
 * @author MSI
 */
public class DoctorList extends AbstractClass{
    public String email;
    public String speciality;
    public String startWorkingTime;
    public String endWorkingTime;
    public DoctorList( String name, String gender, String phoneNumber , String email , String speciality , String start , String end) {
        super(name, gender, phoneNumber);
        this.email            = email;
        this.speciality       = speciality;
        this.startWorkingTime = start;
        this.endWorkingTime   = end;
    } 

    /**
     *
     */
    public void InsertDoctorIntoDB(Connection connection){
        PreparedStatement pre = null;
        String query = "INSERT INTO `doctor`(`name`, `gender`, `phone`, `email`, `speciality`, `start_work`, `end_work`) "
                      +"VALUES (?,?,?,?,?,?,?)";
        try{
            pre = connection.prepareStatement(query);
            pre.setString(1, name);
            pre.setString(2, gender);
            pre.setString(3, phoneNumber);
            pre.setString(4, email);
            pre.setString(5, speciality);
            pre.setString(6, startWorkingTime);
            pre.setString(7, endWorkingTime);
            pre.executeUpdate();
            System.out.println("Data Inserted Into Database ...\n");
        } catch (SQLException e) {
            System.out.println("Error Inserting Data\n");
        }
    }
    
}
