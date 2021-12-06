
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Poised_Final {

    // Main method
    public static void main(String[] args) {

        // Try catch block
        try {

            // Connect to the library_db database, via the jdbc:mysql: channel on localhost (this PC)
            // Use username "otheruser", password "swordfish".
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PoisePMS_db?useSSL=false",
                    "otheruser",
                    "swordfish"
            );

            // Create a direct line to the database for running our queries
            Statement statement = connection.createStatement();
            ResultSet results;
            int rowsAffected;

            Scanner sc = new Scanner(System.in).useDelimiter("\n");

            // Ask user if they want to select from a list or exit
            System.out.println("\nDo you wish to edit one of the tables in the PoisePMS data base yes(y) or no(n) :");
            String answer = sc.next();

            // Use a while loop so the user can select from the list until they select n to exit
            while (!answer.equals("n")) {

                // Output list
                System.out.println("\nPlease select from the following or (n) to exit :\n1  - View ongoing projects  " +
                        "\n2  - View finalized projects " +
                        "\n3  - View personnel involved in the projects " +
                        "\n4  - View invoices of finalized projects" +
                        "\n5  - Enter details of new project " +
                        "\n6  - Enter details of Architect, Contractor or Customer" +
                        "\n7  - Edit due date of the project" +
                        "\n8  - Edit the total amount paid to date on a project" +
                        "\n9  - Do you wish to finalize a project" +
                        "\n10 - Find which projects are overdue ");
                answer = sc.next();


                // Use if statement while user selects 1
                if (answer.equals("1")) {

                    // Output ongoing projects
                    printAllFromProject(statement);
                }

                // Use if statement while user selects 2
                if (answer.equals("2")) {

                    // Output all finalised projects
                    printAllFromProjectComplete(statement);
                }

                // Use if statement while user selects 3
                if (answer.equals("3")) {

                    // Output details of the personnel involved in the projects
                    printAllFromProjectPerson(statement);
                }

                // Use if statement while user selects 4
                if (answer.equals("4")) {

                    // Output the invoices for the projects
                    printAllFromProjectInvoice(statement);
                }

                // Use else if statement while user selects 5
                else if (answer.equals("5")) {

                    // Ask user to input details of new project
                    System.out.println("Please enter details of new project to be added as shown : (projectNum, " +
                            "'projectName', " +
                            "'buildingType', " +
                            "'projectAddress', " +
                            "'erfNum', " +
                            "totalFee, " +
                            "paidToDate, " +
                            "amountDue, " +
                            "'dueDate(yyyy-mm-dd)', " +
                            "'architect', " +
                            "'contractor', " +
                            "'customer')");
                    String newProject = sc.next();

                    // Add a new project to the project table
                    rowsAffected = statement.executeUpdate("INSERT INTO project VALUES " + newProject);
                    System.out.println("Query complete, " + rowsAffected + " rowsadded.");

                    // Output all ongoing projects
                    printAllFromProject(statement);
                }

                // Use else if statement while user selects 6
                else if (answer.equals("6")) {

                    // Ask User to input details of new person to be added to the projectPerson table
                    System.out.println("Please enter details of the person to be added as shown : ('type', 'name', 'project', 'telephoneNum', 'emailAddress', 'postAddress') ");
                    String newPerson = sc.next();

                    // Add a new person:
                    rowsAffected = statement.executeUpdate("INSERT INTO projectPerson VALUES " + newPerson);
                    System.out.println("Query complete, " + rowsAffected + " rowsadded.");

                    printAllFromProjectPerson(statement);
                }

                // Use else if statement while user selects 7
                else if (answer.equals("7")) {

                    // Ask the user to enter the project they want to edit
                    System.out.println(" Please enter the project number you wish to edit : ");
                    int change = sc.nextInt();
                    System.out.println("Please enter the new due date as 'yyyy-mm-dd' ");
                    String newDate = sc.next();

                    // Input the new due date in the project table
                    rowsAffected = statement.executeUpdate("UPDATE project SET  dueDate " + " = " + newDate + " WHERE projectNum = " + change);
                    System.out.println(" Query complete, " + rowsAffected + " rowsupdated.");
                    printAllFromProject(statement);
                }

                // Use else if statement while user selects 8
                else if (answer.equals("8")) {

                    // Ask the user to enter the project they want to edit
                    // Ask the user for the new amount paid by the customer to date
                    System.out.println(" Please enter the project number you wish to edit : ");
                    int change = sc.nextInt();
                    System.out.println("Please enter the new amount paid by the customer to date : ");
                    int newAmount = sc.nextInt();

                    // Change the amount paid to date:
                    rowsAffected = statement.executeUpdate("UPDATE project SET  paidToDate " + " = " + newAmount + " WHERE projectNum = " + change);
                    System.out.println(" Query complete, " + rowsAffected + " rowsupdated.");

                    // Update the amount due by the customer
                    int fee = 0;
                    results = statement.executeQuery("SELECT totalFee FROM project WHERE projectNum = " + change);
                    //Loop over the results, printing them all
                    while (results.next()) {
                        fee = (results.getInt("totalFee"));
                    }

                    // Calculate the new amount due
                    int amountDue = fee - newAmount;
                    System.out.println(amountDue);

                    // Change the total amount paid by the customer:
                    rowsAffected = statement.executeUpdate("UPDATE project SET  amountDue " + " = " + amountDue + " WHERE projectNum = " + change);
                    System.out.println(" Query complete, " + rowsAffected + " rowsupdated.");

                    // Output the ongoing projects
                    printAllFromProject(statement);


                }

                // Use else if statement while user selects 9
                else if (answer.equals("9")) {

                    // Ask the user to enter the project number they wish to edit
                    System.out.println(" Please enter the project number you wish to finalise : ");
                    int change = sc.nextInt();

                    // Update the amount due by the customer
                    int amount = 0;
                    results = statement.executeQuery("SELECT amountDue FROM project WHERE projectNum = " + change);
                    //Loop over the results, printing them all
                    while (results.next()) {
                        amount = (results.getInt("amountDue"));
                    }

                    //Use if statement when customer still has amount due
                    if (amount != 0) {
                        String customer = "";
                        results = statement.executeQuery("SELECT customer FROM project WHERE projectNum = " + change);
                        //Loop over the results, printing them all
                        while (results.next()) {
                            customer = (results.getString("customer"));
                        }

                        // Get the details of the customer from the projectPerson table
                        String telephoneNum = "";
                        results = statement.executeQuery("SELECT telephoneNum FROM projectPerson WHERE name = " + "'" + customer + "'");
                        //Loop over the results, printing them all
                        while (results.next()) {
                            telephoneNum = (results.getString("telephoneNum"));
                        }

                        String emailAddress = "";
                        results = statement.executeQuery("SELECT emailAddress FROM projectPerson WHERE name = " + "'" + customer + "'");
                        //Loop over the results, printing them all
                        while (results.next()) {
                            emailAddress = (results.getString("emailAddress"));
                        }

                        String postAddress = "";
                        results = statement.executeQuery("SELECT postAddress FROM projectperson WHERE name = " + "'" + customer + "'");
                        //Loop over the results, printing them all
                        while (results.next()) {
                            postAddress = (results.getString("postAddress"));
                        }

                        // Insert the invoice into the projectInvoice table
                        rowsAffected = statement.executeUpdate("INSERT INTO projectInvoice VALUES ( " + "'" + customer + "'" + ", "
                                + change + ", "
                                + "'" + telephoneNum + "'" + ", "
                                + "'" + emailAddress + "'" + ", "
                                + "'" + postAddress + "'" + ", "
                                + amount + ")");
                        System.out.println("Query complete, " + rowsAffected + " rowsadded.");
                        printAllFromProjectInvoice(statement);

                    }

                    // Use else statement when the amount due is zero and no invoice is required
                    else {
                        System.out.println(" The project " + change + "is fully paid up and does not need an invoice ");
                    }

                    // select the project to insert into projectComplete
                    //Loop over the results, printing them all
                    String project = "";
                    results = statement.executeQuery("SELECT projectNum, projectName, buildingType, projectAddress, erfNum, totalFee, paidToDate, amountDue, dueDate, architect, contractor, customer  FROM project WHERE projectNum = " + change);
                    while (results.next()) {
                        project = "(" + results.getInt("projectNum") + ", "
                                + "'" + results.getString("projectName") + "'" + ", "
                                + "'" + results.getString("buildingType") + "'" + ", "
                                + "'" + results.getString("projectAddress") + "'" + ", "
                                + "'" + results.getString("erfNum") + "'" + ", "
                                + results.getInt("totalFee") + ", "
                                + results.getInt("paidToDate") + ", "
                                + results.getInt("amountDue") + ", "
                                + "'" + results.getString("dueDate") + "'" + ", "
                                + "'" + results.getString("architect") + "'" + ", "
                                + "'" + results.getString("contractor") + "'" + ", "
                                + "'" + results.getString("customer") + "'";
                    }

                    // Get today's date using DateTimeFormatter and localDateTime.now reference codeGrepper.com
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDateTime now = LocalDateTime.now();
                    String todayDate = (dtf.format(now));

                    // Insert the finalised project into projectComplete
                    rowsAffected = statement.executeUpdate("INSERT INTO projectComplete VALUES " + project + ", " + "'" + todayDate + "'" + ")");
                    System.out.println("Query complete, " + rowsAffected + " rowsadded.");
                    printAllFromProjectComplete(statement);

                    // Delete the finalised project from project:
                    rowsAffected = statement.executeUpdate("DELETE FROM project WHERE projectNum = " + change);
                    System.out.println("Query complete, " + rowsAffected + " rowsremoved.");
                    printAllFromProject(statement);
                }

                // Use else if statement while user selects 10
                else if (answer.equals("10")) {

                    // Ask the user to enter the project number they wish to edit
                    System.out.println(" Do you wish to find which projects are over due if yes enter (y) or no enter (0) to exit ");
                    String overDue = sc.next();

                   // Try Catch Block
                    try {

                        // Use while loop to check if projects are overdue
                        while (!overDue.equals("0")) {

                            // Output ongoing projects
                            printAllFromProject(statement);

                            // Ask user to input the project number
                            System.out.println("\nPlease enter the project number you wish to finalise or enter  (0) to exit ");
                            overDue = sc.next();

                            // Use if statement when overDue does not equal zero
                            if (!overDue.equals("0")) {

                                // Extract the dueDate from the project and save as date
                                //Loop over the results, printing them all
                                String date = "";
                                results = statement.executeQuery("SELECT dueDate FROM project WHERE projectNum = " + overDue);
                                while (results.next()) {
                                    date = (results.getString("dueDate"));
                                }
                                System.out.println(date);

                                // Get today's date using DateTimeFormatter and localDateTime.now reference codeGrepper.com
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDateTime now = LocalDateTime.now();
                                String todayDate = (dtf.format(now));

                                // Comparing dates using sdformat.parse to be able to compare the dates reference tutorialspoint.com
                                SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                                Date d1 = sdformat.parse(date);
                                Date d2 = sdformat.parse(todayDate);

                                // Use if statement when the due date of the project is before today's date
                                if (d1.compareTo(d2) < 0) {
                                    System.out.println("This project is overdue\n");
                                }
                            }

                            // Use else statement to exit while loop
                            else{
                                System.out.println("You have chosen to exit");
                            }
                            }

                        // Catch block for while loop
                        } catch (Exception e) {
                        System.out.println("Error1");
                    }

                }

                // Use else statement to exit program
                else{
                System.out.println("You have chosen to exit");
                }
            }

            // Catch block if a string is input instead of an integer
        }    catch (InputMismatchException e) {
            System.out.println(" You have entered a non numeric field value");
        }

        // Catch block for general errors
        catch (Exception e) {
            System.out.println("Error");

        }
    }

    // Method printing all values in all rows.
    // Takes a statement to try to avoid spreading DB access too far.
    // @param a statement on an existing connection
    // @throws SQLException
    public static void printAllFromProject(Statement statement) throws SQLException {
        ResultSet results = statement.executeQuery("SELECT * FROM project");

        // Loop over the results, printing them all
        while (results.next()) {
            System.out.println(results.getInt("projectNum") + ", "
                    + results.getString("projectName") + ", "
                    + results.getString("buildingType") + ", "
                    + results.getString("projectAddress") + ", "
                    + results.getString("erfNum") + ", "
                    + results.getInt("totalFee") + ", "
                    + results.getInt("paidToDate") + ", "
                    + results.getInt("amountDue") + ", "
                    + results.getString("dueDate") + ", "
                    + results.getString("architect") + ", "
                    + results.getString("contractor") + ", "
                    + results.getString("customer"));
        }
    }

    // Method printing all values in all rows.
    // Takes a statement to try to avoid spreading DB access too far.
    // @param a statement on an existing connection
    // @throws SQLException
    public static void printAllFromProjectComplete(Statement statement) throws SQLException {
        ResultSet results = statement.executeQuery("SELECT * FROM projectComplete");

        // Loop over the results, printing them all
        while (results.next()) {
            System.out.println(results.getInt("projectNum") + ", "
                    + results.getString("projectName") + ", "
                    + results.getString("buildingType") + ", "
                    + results.getString("projectAddress") + ", "
                    + results.getString("erfNum") + ", "
                    + results.getInt("totalFee") + ", "
                    + results.getInt("paidToDate") + ", "
                    + results.getInt("amountDue") + ", "
                    + results.getString("dueDate") + ", "
                    + results.getString("architect") + ", "
                    + results.getString("contractor") + ", "
                    + results.getString("customer") + ", "
                    + results.getString("dateFinalised"));
        }
    }

    // Method printing all values in all rows.
    // Takes a statement to try to avoid spreading DB access too far.
    // @param a statement on an existing connection
    // @throws SQLException
    public static void printAllFromProjectPerson(Statement statement) throws SQLException {
        ResultSet results = statement.executeQuery("SELECT * FROM projectPerson");

        // Loop over the results, printing them all
        while (results.next()) {
            System.out.println(results.getString("type") + ", "
                    + results.getString("name") + ", "
                    + results.getString("project") + ", "
                    + results.getString("telephoneNum") + ", "
                    + results.getString("emailAddress") + ", "
                    + results.getString("postAddress"));
        }
    }

    // Method printing all values in all rows.
    // Takes a statement to try to avoid spreading DB access too far.
    // @param a statement on an existing connection
    // @throws SQLException
    public static void printAllFromProjectInvoice(Statement statement) throws SQLException {
        ResultSet results = statement.executeQuery("SELECT * FROM projectInvoice");

        // Loop over the results, printing them all
        while (results.next()) {
            System.out.println(results.getString("name") + ", "
                    + results.getInt("projectNum") + ", "
                    + results.getString("telephoneNum") + ", "
                    + results.getString("emailAddress") + ", "
                    + results.getString("postalAddress") + ", "
                    + results.getInt("amountDue"));
        }
    }

}
