package jdbc;
import java.sql.*;
import java.io.*;
import java.util.Scanner;

public class BankingManagementSystem {
    public static void main(String[] args) throws IOException {
        Connection con = null;
        Statement stmt = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Create the connection object
            String conurl = "jdbc:oracle:thin:@localhost:1521:xe";
            con = DriverManager.getConnection(conurl, "system", "1234");
            stmt = con.createStatement();

            int choice;
            do {
                System.out.println("\n\n***** Banking Management System *****");
                System.out.println("1. Show Customer Records");
                System.out.println("2. Add Customer Record");
                System.out.println("3. Delete Customer Record");
                System.out.println("4. Update Customer Information");
                System.out.println("5. Show Account Details of a Customer");
                System.out.println("6. Show Loan Details of a Customer");
                System.out.println("7. Deposit Money to an Account");
                System.out.println("8. Withdraw Money from an Account");
                System.out.println("9. Exit");
                System.out.print("Enter your choice (1-9): ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        showCustomerRecords(stmt);
                        break;
                    case 2:
                        addCustomerRecord(stmt, scanner);
                        showCustomerRecords(stmt);
                        break;
                    case 3:
                        deleteCustomerRecord(stmt, scanner);
                        showCustomerRecords(stmt);
                        break;
                    case 4:
                        updateCustomerRecord(stmt, scanner);
                        showCustomerRecords(stmt);
                        break;
                    case 5:
                        showAccountDetails(stmt, scanner);
                        break;
                    case 6:
                        showLoanDetails(stmt, scanner);
                        break;
                    case 7:
                        depositMoney(stmt, scanner);
                        showAccountDetails(stmt, scanner);
                        break;
                    case 8:
                        withdrawMoney(stmt, scanner);
                        showAccountDetails(stmt, scanner);
                        break;
                    case 9:
                        System.out.println("Exiting the program.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 9);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }

    private static void showCustomerRecords(Statement stmt) throws SQLException {
        String query = "SELECT * FROM Customer";
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("\nCustomer Records:");
        while (rs.next()) {
            System.out.println("Customer No: " + rs.getString("cust_no"));
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Phone No: " + rs.getString("phoneno"));
            System.out.println("City: " + rs.getString("city"));
            System.out.println("-------------------------------");
        }
        rs.close();
    }

    private static void addCustomerRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer Number: ");
        String custNo = scanner.next();
        System.out.print("Enter Name: ");
        String name = scanner.next();
        System.out.print("Enter Phone Number: ");
        String phoneNo = scanner.next();
        System.out.print("Enter City: ");
        String city = scanner.next();

        String query = "INSERT INTO Customer (cust_no, name, phoneno, city) VALUES ('" + custNo + "', '" + name + "', '" + phoneNo + "', '" + city + "')";
        stmt.executeUpdate(query);
        System.out.println("Customer record added successfully.");
    }

    private static void deleteCustomerRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer Number to delete: ");
        String custNo = scanner.next();

        String query = "DELETE FROM Customer WHERE cust_no = '" + custNo + "'";
        stmt.executeUpdate(query);
        System.out.println("Customer record deleted successfully.");
    }

    private static void updateCustomerRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer Number to update: ");
        String custNo = scanner.next();
        System.out.println("Enter 1: For Name 2: For Phone No 3: For City to update:");
        int updateChoice = scanner.nextInt();

        String column = null;
        String newValue = null;

        switch (updateChoice) {
            case 1:
                System.out.print("Enter new Name: ");
                column = "name";
                newValue = scanner.next();
                break;
            case 2:
                System.out.print("Enter new Phone No: ");
                column = "phoneno";
                newValue = scanner.next();
                break;
            case 3:
                System.out.print("Enter new City: ");
                column = "city";
                newValue = scanner.next();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        String query = "UPDATE Customer SET " + column + " = '" + newValue + "' WHERE cust_no = '" + custNo + "'";
        stmt.executeUpdate(query);
        System.out.println("Customer record updated successfully.");
    }

    private static void showAccountDetails(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer Number to view account details: ");
        String custNo = scanner.next();

        String query = "SELECT * FROM Account WHERE cust_no = '" + custNo + "'";
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("\nAccount Details:");
        while (rs.next()) {
            System.out.println("Account No: " + rs.getString("account_no"));
            System.out.println("Type: " + rs.getString("type"));
            System.out.println("Balance: " + rs.getDouble("balance"));
            System.out.println("Branch Code: " + rs.getString("branch_code"));
            System.out.println("Branch Name: " + rs.getString("branch_name"));
            System.out.println("Branch City: " + rs.getString("branch_city"));
            System.out.println("-------------------------------");
        }
        rs.close();
    }

    private static void showLoanDetails(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer Number to view loan details: ");
        String custNo = scanner.next();

        String query = "SELECT * FROM Loan WHERE cust_no = '" + custNo + "'";
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("\nLoan Details:");
        while (rs.next()) {
            System.out.println("Loan No: " + rs.getString("loan_no"));
            System.out.println("Amount: " + rs.getDouble("amount"));
            System.out.println("Branch Code: " + rs.getString("branch_code"));
            System.out.println("Branch Name: " + rs.getString("branch_name"));
            System.out.println("Branch City: " + rs.getString("branch_city"));
            System.out.println("-------------------------------");
        }
        rs.close();
    }

    private static void depositMoney(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Account Number to deposit money: ");
        String accountNo = scanner.next();
        System.out.print("Enter Amount to deposit: ");
        double amount = scanner.nextDouble();

        String query = "UPDATE Account SET balance = balance + " + amount + " WHERE account_no = '" + accountNo + "'";
        stmt.executeUpdate(query);
        System.out.println("Money deposited successfully.");
    }

    private static void withdrawMoney(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Account Number to withdraw money: ");
        String accountNo = scanner.next();
        System.out.print("Enter Amount to withdraw: ");
        double amount = scanner.nextDouble();

        String balanceQuery = "SELECT balance FROM Account WHERE account_no = '" + accountNo + "'";
        ResultSet rs = stmt.executeQuery(balanceQuery);

        if (rs.next()) {
            double currentBalance = rs.getDouble("balance");
            if (currentBalance >= amount) {
                String query = "UPDATE Account SET balance = balance - " + amount + " WHERE account_no = '" + accountNo + "'";
                stmt.executeUpdate(query);
                System.out.println("Money withdrawn successfully.");
            } else {
                System.out.println("Insufficient balance.");
            }
        }
        rs.close();
    }
}
