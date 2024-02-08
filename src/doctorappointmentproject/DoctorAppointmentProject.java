//project package
package doctorappointmentproject;

//library
import java.sql.*;
import java.util.Scanner;

public class DoctorAppointmentProject {

    /**
     *
     * @param connection
     * @param spaces
     */
    public static void GetDataFromDB(Connection connection,String spaces){
        try {
            String query = "SELECT * FROM `doctor` WHERE 1";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                
                while(resultSet.next()){
                    System.out.print(spaces+"________________________________________________________________________________________________________________________________________________\n");
                    System.out.print("\n"+spaces+ resultSet.getInt("id") + spaces + resultSet.getString("name") + spaces + resultSet.getString("gender") + spaces + resultSet.getString("phone") + spaces + resultSet.getString("email") + spaces + resultSet.getString("speciality") + spaces + resultSet.getString("start_work") + spaces + resultSet.getString("end_work") + spaces +resultSet.getBoolean("isAvailbility") +"\n");
                }
                statement.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()+"Error ...");
        }
    }
    public static void main(String[] args) {
        //initialize require object 
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        Connection connection = null;
//        DoctorList doctorList = null;
        //declare variable 
        byte option;
        byte subOption;
        int numspaces = 10;
        String spaces = String.format("%"+ numspaces + "s", "");
        //db connection required
        String dbUrl = "jdbc:mysql://localhost:3307/java_database";
        String username = "root";
        String password = "";
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl,username,password);
            System.out.print("\t\t\t\t\t\t==============================================\n");
            System.out.print("\t\t\t\t\t\t      Successfully Connecting To Database\n");
            System.out.print("\t\t\t\t\t\t==============================================\n");
        } 
        catch(ClassNotFoundException | SQLException e) {
            System.out.print("Error Connection With Database\n");
        }
        do {            
            System.out.print("------------> Doctor's Appointment Scheduler <------------\n");
            System.out.print("1.Doctor's Information Management\n");
            System.out.print("2.Patient's Management\n");
            System.out.print("----------------------------------------------------------\n");
            System.out.print("Your Choice = "); option = scanner.nextByte();
            switch(option){
                case 1 -> {
                    do {                        
                        System.out.print("===============> Doctor's CRUD Operation <==============\n");
                        System.out.print("1. Register Doctor's Information\n");
                        System.out.print("2. List Of Doctor's Information\n");
                        System.out.print("3. Edit Doctor's Information\n");
                        System.out.print("4. Remove Doctor's Information\n");
                        System.out.print("==========================================================\n");
                        System.out.print("Your Choice = "); subOption = scanner.nextByte();
                        switch(subOption){
                            case 1 -> {
                                System.out.print("Please Register Doctor's Information Below : \n");
                                System.out.print("1. Name        = "); String name   = scanner.next();
                                System.out.print("2. Gender      = "); String gender = scanner.next();
                                System.out.print("3. Phone       = "); String phone  = scanner.next();
                                System.out.print("4. Email       = "); String email  = scanner.next();
                                System.out.print("5. Speciality  = "); String specs  = scanner.next();
                                System.out.print("6. Start Time  = "); String start  = scanner.next();
                                System.out.print("7. End Time    = "); String end    = scanner.next();
                                DoctorList doctorList = new DoctorList(name, gender, phone, email, specs, start, end);
                                doctorList.InsertDoctorIntoDB(connection);
                            }
                            case 2 -> {
                                System.out.print("\t\t\t\t\t\tList All Doctor's Information\n");
                                System.out.print("\n"+spaces+ "ID" + spaces + "Name" + spaces + "Gender" + spaces + "Phone Number" + spaces + "Email" + spaces + "Speciality" + spaces + "Start Time" + spaces + "End Time" + spaces + "IsAvailibity\n");                                
                                GetDataFromDB(connection, spaces);
                            }
                            case 3 -> {
                                System.out.print("\t\t\t\t\t\tEdit Doctor's Information\n");
                                System.out.print("Search Doctor For Update = "); byte id = scanner.nextByte();
                                try(Statement statement = connection.createStatement()) {
                                    String queryRead   = "SELECT `id` from `doctor` WHERE 1";
                                    String queryUpdate = "UPDATE `doctor` SET `name`=? ,`gender`=?,`phone`=?,`email`=?,`speciality`=?,`start_work`=?,`end_work`=? WHERE `id` = ?";
                                    ResultSet resultSet = statement.executeQuery(queryRead);
                                    while(resultSet.next()){
                                        if(id == resultSet.getInt("id")){
                                            System.out.print("Edit Doctor's Information\n");
                                            System.out.print("1. Name        = "); String name   = scanner.next();
                                            System.out.print("2. Gender      = "); String gender = scanner.next();
                                            System.out.print("3. Phone       = "); String phone  = scanner.next();
                                            System.out.print("4. Email       = "); String email  = scanner.next();
                                            System.out.print("5. Speciality  = "); String specs  = scanner.next();
                                            System.out.print("6. Start Time  = "); String start  = scanner.next();
                                            System.out.print("7. End Time    = "); String end    = scanner.next();
                                            try(PreparedStatement statement1 = connection.prepareStatement(queryUpdate)) {
                                                statement1.setString(1, name);
                                                statement1.setString(2,gender);
                                                statement1.setString(3,phone);
                                                statement1.setString(4, email);
                                                statement1.setString(5, specs);
                                                statement1.setString(6, start);
                                                statement1.setString(7, end);
                                                statement1.setInt(8,id);
                                                
                                                int rowEffected = statement1.executeUpdate();
                                                System.out.println("Row Effected : " + rowEffected);
                                                
                                            } catch (Exception e) {
                                                System.out.print(e.getMessage());
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    System.out.print(e.getMessage());
                                }
                            }
                            case 4 -> {
                               
                                System.out.println("Remove Doctor's Information");
                                System.out.print("Search Doctor for Removing "); byte id = scanner.nextByte();
                                try(Statement statement = connection.createStatement()){
                                    String queryRead = "SELECT `id` FROM `doctor` WHERE 1";
                                    String queryDelete = "DELETE FROM `doctor` WHERE `id` = ?";
                                    ResultSet resultSet = statement.executeQuery(queryRead);
                                    while(resultSet.next()){
                                        if(id == resultSet.getInt("id")){
                                            try{
                                                PreparedStatement statement2 = connection.prepareStatement(queryDelete);
                                                statement2.setInt(1, id);
                                                
                                                int rowEffected = statement2.executeUpdate();
                                                System.out.println("Row Effected " + rowEffected);
                                            } catch (Exception e) {
                                                System.out.println("Error\n");
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
                            default -> System.out.print("Invaild Choice\n");
                        }
                    } while (subOption != 5);   
                }
                case 2 -> {
                }
            }
        } while (option != 3);
    }

}