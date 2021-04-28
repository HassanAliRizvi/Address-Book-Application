package Address_Book;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class App extends Database {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        displayMenu();
    }


    /**
     displayMenu connects the Database.java file and formats it into a proper UI display. It calls all the functions from database.java
     and allows user to choose between deleting, modifying, or adding users.
     **/
    public static void displayMenu() throws IOException, SQLException, ClassNotFoundException {
        while(true) {
            Scanner input = new Scanner(System.in);
            Database test = new Database();
            System.out.println("");
            System.out.println("");

            System.out.println("What would you like to do?(1,2,3....etc)");
            System.out.print("""
                    **** A D D R E S S B O O K ****
                    1. Print Entries
                    2. Add new entry
                    3. Delete entry
                    4. Modify entry
                    5. Quit
                    """);
            System.out.print("Please select an option: ");
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
    // printing entries
                case 1 -> {
                    test.printEntries();


                }
    // adding an entry in the file
                case 2 -> {
                    System.out.println("");
                    test.printEntries();
                    System.out.println("");
                    System.out.println("These are all the users listed in the database. Please fill out the following information to add a user...");
                    System.out.println("");
                    add_entry();
                    System.out.println("User has been added. Please check the database to verify...");
                    System.out.println("");
                    test.printEntries();

                }
    // deleting a entry from a file
                case 3 -> {
                    System.out.println("");
                    test.printEntries();//close
                    deleteEntry();
                    System.out.println("");
                    test.printEntries();//error here since


                }
    // modifying an entry from a file
                case 4 -> {
                    System.out.println("");
                    test.printEntries();
                    System.out.println("");
                    System.out.println("These are all the users listed in the database. Please fill out the following information to add a user...");
                    System.out.println("");
                    modifyEntry();
                    System.out.println("User has been modified. Please check the database to verify...");
                    System.out.println("");
                    test.printEntries();
                }

    //quitting from the program
                case 5 -> {
                    System.out.println("Good bye!");
                    System.exit(0);
                    break;
                }
                default -> System.out.println("Invalid option");
            }
        }

    }
}
