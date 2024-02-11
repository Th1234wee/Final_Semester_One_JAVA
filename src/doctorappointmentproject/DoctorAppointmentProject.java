//project package
package doctorappointmentproject;

//library
import java.sql.*;
import java.time.LocalTime;
import java.util.Random;
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
    public static void SearchForAvailableDoctor(Connection connection,String name,String gender,String phone,String time,String specs){
        Random random = new Random();
        String queryRead = "SELECT * FROM `doctor` WHERE 1";
        try(Statement statement = connection.createStatement()) {
            LocalTime Time = LocalTime.parse(time);
            ResultSet resultSet = statement.executeQuery(queryRead);
            while(resultSet.next()){
                LocalTime startTime = LocalTime.parse(resultSet.getString("start_work"));
                LocalTime endTime   = LocalTime.parse(resultSet.getString("end_work"));
                if(Time.isAfter(startTime) && Time.isBefore(endTime) &&  (specs.compareToIgnoreCase(resultSet.getString("speciality")) == 0)){
                    System.out.println("\nYour Appoinment will be start by following information : ");
                    System.out.println("-------------------------------------------------------------------------------\n");
                    System.out.println("The doctor that will handle this appoinment : "+resultSet.getString("name")+"\n");
                    int generatedCode = random.nextInt(99999);
                    System.out.println("Your Appointment code is : " + generatedCode + "\n");
                    System.out.println("Please Bring it when you come to appointment\n");
                    System.out.println("-------------------------------------------------------------------------------\n");
                    String insertIntoTablePatientQuery = "INSERT INTO `patient`(`name`, `gender`, `phone`, `disease`, `appointment_time`,`appointment_code`,`doctor_id`) VALUES (?,?,?,?,?,?,?)";
                    try(PreparedStatement preparedStatement = connection.prepareStatement(insertIntoTablePatientQuery)) {
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, gender);
                        preparedStatement.setString(3, phone);
                        preparedStatement.setString(4, specs);
                        preparedStatement.setString(5, time);
                        preparedStatement.setInt(6, generatedCode);
                        preparedStatement.setString(7, resultSet.getString("id"));
                        preparedStatement.executeUpdate();
                        System.out.println("Information is synced with Database");
                    } catch (SQLException e) {
                        System.out.println("Error : " + e.getMessage());
                    }
                } 
            }
        } catch (Exception e) {
            System.out.println("Error : "+ e.getMessage());
        }
    }
    public static void main(String[] args){
        //initialize require object 
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        Connection connection = null;
        //declare variable 
        byte option = 0;
        byte subOption = 0;
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
                        System.out.print("5.Exit\n");
                        System.out.print("==========================================================\n");
                        System.out.print("Your Choice = "); subOption = scanner.nextByte();
                        switch(subOption){
                            case 1 -> {
                                System.out.print("Please Register Doctor's Information Below : \n");
                                System.out.print("1. Name               = "); String name   = scanner.next();
                                System.out.print("2. Gender             = "); String gender = scanner.next();
                                System.out.print("3. Phone              = "); String phone  = scanner.next();
                                System.out.print("4. Email              = "); String email  = scanner.next();
                                System.out.print("5. Speciality         = "); String specs  = scanner.next();
                                System.out.print("6. Start Time(MM:SS)  = "); String start  = scanner.next();
                                System.out.print("7. End Time(MM:SS)    = "); String end    = scanner.next();   
                                DoctorList doctorList = new DoctorList(name, gender, phone, email, specs, start, end);
                                doctorList.InsertDoctorIntoDB(connection);
                                break;
                            }
                            case 2 -> {
                                System.out.print("\t\t\t\t\t\tList All Doctor's Information\n");
                                System.out.print("\n"+spaces+ "ID" + spaces + "Name" + spaces + "Gender" + spaces + "Phone Number" + spaces + "Email" + spaces + "Speciality" + spaces + "Start Time" + spaces + "End Time" + spaces + "IsAvailibity\n");                                
                                GetDataFromDB(connection, spaces);
                                break;
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
                                break;
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
                                                System.out.println("Row Effected = " + rowEffected);
                                            } catch (SQLException e) {
                                                System.out.println("Error\n");
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error : "+ e.getMessage());
                                }
                                break;
                            }
                            case 5->{
                                System.exit(0);
                            }
                            default -> System.out.print("Invaild Choice\n");
                        }
                    } while (subOption != 5);   
                }
                case 2 -> {
                    do{
                        System.out.println("===============> Patient's Appointment Preparation <==============\n");
                        System.out.println("1. Making Appointment");
                        System.out.println("2. Start Appointment");
                        System.out.println("3. Exit");
                        System.out.println("==================================================================");
                        System.out.print("Your Choice = "); subOption = scanner.nextByte();
                        switch(subOption){
                            case 1 -> {
                                System.out.println("For Registration's Requirement Please Provide Us a Valid Information : ");
                                System.out.print("1. Name                               = "); String name     = scanner.next();
                                System.out.print("2. Gender                             = "); String gender   = scanner.next();
                                System.out.print("3. Phone                              = "); String phone    = scanner.next();
                                System.out.print("4. Disease's Description              = "); String disease  = scanner.next();
                                System.out.print("5. Time for Appointment(MM:SS)        = "); String time     = scanner.next();
                                System.out.println("------------------------------------------------------------------------");
                                System.out.print("Please Wait A Moment , We set everything ready for you...!");
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    System.out.println("error : " + e.getMessage());
                                }
                                SearchForAvailableDoctor(connection,name,gender,phone, time, disease);
                                break;
                            }
                            case 2 -> {
                                String query = "SELECT * FROM `patient` WHERE 1";
                                System.out.print("Please Provide Your Appointment Code = "); int code = scanner.nextInt();
                                try(Statement statement = connection.createStatement()) {
                                    ResultSet resultSet = statement.executeQuery(query);
                                    while(resultSet.next()){
                                        if(code == resultSet.getInt("appointment_code")){
                                            System.out.println("Please wait a moment ...!");
                                            Thread.sleep(3000);
                                            System.out.println("Please follow us to meet the doctor...!");
                                            String updateStatusQuery = "UPDATE `patient` SET `isFinished` = ? WHERE `appointment_code` = ?";
                                            try(PreparedStatement statement1 = connection.prepareStatement(updateStatusQuery)) {
                                              statement1.setBoolean(1,true);
                                              statement1.setInt(2, code);
                                              System.out.println("Appointment Finished...!");
                                            }catch (SQLException e) {
                                                System.out.println("Error : " + e.getMessage());
                                            }
                                        }
                                        else{
                                            System.out.println("Invalid Code...!");
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error : " + e.getMessage());
                                }
                               break; 
                            }
                            case 3->{
                                System.exit(0);
                            }
                        }
                    }while(subOption != 3);
                }
            }
        } while (option != 3);
    }

}