package Address_Book;

import java.sql.*;
import java.util.Scanner;
import java.sql.ResultSet;

//TO-DO: 2 last things:-
// 1) Delete entry, 2) modify entry, 3) format the code.

public class Database {
    //establishing connection to a database
    private static Connection conn;

    //use this boolean value to see if there's a table created
    private static boolean hasData = false;



    //displays the user from the database
    public ResultSet displayUsers() throws SQLException, ClassNotFoundException {
        //checking to see if there's a connection
        if (conn == null || conn.isClosed()) {
            getConnection();
        }

        //Once connection is created.
        //We select the required fields from the database
        Statement state = conn.createStatement();
        ResultSet res = state.executeQuery("SELECT id, firstName, middleName, lastName, email, phoneNumber FROM user");
        return res;
    }

    //gets connection from the jdbc file in the desktop to ensure that it's connected to the database
    private static void getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:AddressBookDatabase.db");
        initialize();
    }

    //initializes a SQLite table which has an id, first name, last name, phone, and email
    private static void initialize() throws SQLException {
        if (!hasData) {
            hasData = true;

            //creates a connection with the database system to write SQL code
            Statement state = conn.createStatement();
            ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");//a master table that keeps track of users and the database.
            if (!res.next())  {
                System.out.println("Building the User table with values");

                //create the table
                Statement state2 = conn.createStatement();
                state2.execute("CREATE TABLE user(id integer, firstName varchar(60), middleName varchar(60),lastName varchar(60), phoneNumber varchar(22), email varchar(60), PRIMARY KEY(id) )");
            }
        }
    }


     /**
     this function adds a user to the database. It uses SQLite command to insert the data into AddressBookDatabase.db
     **/
    public static void addUser(String firstname, String middlename, String lastname, String email, String phoneNumber) throws ClassNotFoundException, SQLException {
        if (conn == null || conn.isClosed()) {
            getConnection();
        }

        //the prep variable is used to establish connection to the database to write SQLITE code
        PreparedStatement prep = conn.prepareStatement("INSERT INTO user(firstName, middleName, lastName, email, phoneNumber) values(?,?,?,?,?)");

        //the following commands use the parameter index established in the table when created to add user values
        prep.setString(1, firstname);
        prep.setString(2, lastname);
        prep.setString(3, middlename);
        prep.setString(4, email);
        prep.setString(5, phoneNumber);
        prep.execute();

    }


    /**
     add_entry function allows user to interact with the database. It's the 'UI display' for addUser function
     **/
    public static void add_entry() {
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the first name of the user: ");
        String firstName = input.next();
        System.out.print("Please enter the middle name of the user: ");
        String middleName = input.next();
        System.out.print("Please enter the last name of the user: ");
        String lastName = input.next();
        System.out.print("Please enter the email of the user: ");
        String email = input.next();
        System.out.print("Please enter the phone number of the user: ");
        String phoneNumber = input.next();
        try {
            addUser(firstName, lastName, middleName, email, phoneNumber);
        }
        catch (ClassNotFoundException | SQLException cex){}
    }


    /**
     delete_User function deletes a user from the table using SQLite command. It also checks whether the table is empty as you can't delete
     in a empty table
     **/
    public static void deleteUser(String user_input) throws SQLException, ClassNotFoundException {
        if (conn == null) {
            getConnection();
        }


        try {
            String sql_delete_command = "DELETE FROM user WHERE EMAIL = ?";

            //giving an error however when I type any other name except "user"
            //the code gives an error of array out of bounds

            PreparedStatement prep = conn.prepareStatement(sql_delete_command);
            prep.setString(1,user_input);
            prep.executeUpdate();
            conn.close();
            System.out.println("Entry has been deleted. Please check the database to verify.");
        } catch (SQLException ex){
            System.out.println(ex.toString());
        }

    }

    /**
     deleteEntry allows user to interact with the database to delete the required user
     **/
    public static void deleteEntry() throws SQLException, ClassNotFoundException {
        //TO-DO: Check if database is empty to allow user to enter a value before deleting anything

        Scanner input = new Scanner(System.in);
        System.out.print("These are all the users listed in the database. Please select the email from the table you want to delete: ");
        String user_input = input.next();
        try {
            deleteUser(user_input);
        }
        catch (ClassNotFoundException | SQLException cex){}

    }



    /**
     modifyUser function modifies all the required fields of a user. For example, changes the first name, lastname, middle name, email and phone number
     **/
    public static void modifyUser(String id, String firstName, String middleName, String lastName, String email, String phoneNumber) throws SQLException, ClassNotFoundException {
        if (conn == null || conn.isClosed()) {
            getConnection();
        }

        String sql_replace_command =
                "UPDATE user SET firstName = ? , middleName = ? , lastName = ? , email = ? , phoneNumber = ? WHERE id = ?";
        PreparedStatement prep = conn.prepareStatement(sql_replace_command);
        prep.setString(6, id);
        prep.setString(1, firstName);
        prep.setString(2, middleName);
        prep.setString(3, lastName);
        prep.setString(4, email);
        prep.setString(5, phoneNumber);
        prep.execute();
        System.out.println("Entry has been modified....");
    }

    /**
     modifyEntry function allows user to interact with the database so that the user can modify a user.
     **/
    public static void modifyEntry() {
        Scanner input = new Scanner(System.in);
        System.out.print("Please select the ID from the table you want to modify: ");
        String user_input = input.next();
        System.out.print("Please enter the new first name of the user: ");
        String firstName = input.next();
        System.out.print("Please enter the new middle name of the user: ");
        String middleName = input.next();
        System.out.print("Please enter the new last name of the user: ");
        String lastName = input.next();
        System.out.print("Please enter the new email of the user: ");
        String email = input.next();
        System.out.print("Please enter the new phone number of the user: ");
        String phoneNumber = input.next();
        try {
            modifyUser(user_input,firstName,middleName,lastName,email,phoneNumber);
        }
        catch (ClassNotFoundException | SQLException cex){}

    }

    /**
     printEntries is a function which prints all the fields in the database so that the user is aware of the fields he/she is deleting or modifying.
     **/
    public void printEntries() throws SQLException, ClassNotFoundException {
        ResultSet rs;

        rs =displayUsers();
        System.out.println("ID"
                + "\t\t\t" + "First Name"
                + "\t\t" + "Middle Name"
                + "\t\t" + "Last Name"
                + "\t\t" + "Email"
                + "\t\t\t\t" + "Phone Number");
        while (rs.next()) {
            System.out.println(rs.getInt("id")
                    + "\t\t\t" + rs.getString("firstName")
                    + "\t\t\t" + rs.getString("middleName")
                    + "\t\t\t" + rs.getString("lastName")
                    + "\t\t\t" + rs.getString("email")
                    + "\t\t\t" + rs.getString("phoneNumber"));
            //rs.string("id???")
        }
    }


}
