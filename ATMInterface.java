import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ATMInterface {
    private static ArrayList<Account> accs = new ArrayList<>();
    private static Account currentAcc;
    private static ArrayList<Transaction> transacHistory = new ArrayList<>();

    public static void main(String[] args) {
        // Add some user data for testing
        Account acc1 = new Account("11011", "1234", 1500.0);
        Account acc2 = new Account("22022", "2468", 2000.0);
        accs.add(acc1);
        accs.add(acc2);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the ATM!");

        do {
            System.out.print("Enter your Account Number: ");
            String enteredAccNo = scanner.next();

            System.out.print("Enter your ATM PIN: ");
            String enteredPinNo = scanner.next();

            if (authenticateUser(enteredAccNo, enteredPinNo)) {
                System.out.println("Authentication successful!");
                displayingMenu(scanner);
            } else {
                System.out.println("Authentication failed. Please try again.");
            }
        } while (true);
    }

    private static boolean authenticateUser(String enteredAccNo, String enteredPinNo) {
        for (Account acc : accs) {
            if (acc.gettingAccNo().equals(enteredAccNo) && acc.gettingPinNo().equals(enteredPinNo)) {
                currentAcc = acc;
                return true;
            }
        }
        return false;
    }

    private static void displayingMenu(Scanner scanner) {
        int choice;
    
        do {
            System.out.println("\n******** Main Menu ********");
            System.out.println("1. Transactions History");
            System.out.println("2. Withdrawal");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Account Details");
            System.out.println("6. Quit");
            System.out.print("Enter your choice: ");
    
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option (1-6).");
                scanner.nextLine();
                continue;
            }
    
            switch (choice) {
                case 1:
                    displayingTransactions();
                    break;
                case 2:
                    performingWithdrawal(scanner);
                    break;
                case 3:
                    performingDeposit(scanner);
                    break;
                case 4:
                    performingTransfer(scanner);
                    break;
                case 5:
                    displayingAccDetails();
                    break;
                case 6:
                    System.out.println("Thank you for using the ATM!");
                    System.exit(0); // Terminate the program
                    break;                
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (true);
    }   

    private static void displayingTransactions() {
        if (transacHistory.isEmpty()) {
            System.out.println("No transaction history found.");
        } else {
            System.out.println("\nTransaction History:");
            System.out.println("Date\t\tType\tAmount");
    
            for (int i = transacHistory.size() - 1; i >= 0; i--) {
                Transaction transaction = transacHistory.get(i);
                System.out.println(
                        transaction.getDate() + "\t" +
                                transaction.getType() + "\t$" +
                                transaction.getAmount()
                );
            }
        }
    }

    private static void recordingTransactions(String type, double amount) {
        Transaction transaction = new Transaction(type, amount, new Date());
        transacHistory.add(transaction);
    }

    private static void performingWithdrawal(Scanner scanner) {
        System.out.print("Enter the amount to withdraw: ");
        double amount = scanner.nextDouble();

        if (amount > 0 && amount <= currentAcc.gettingBalance()) {
            currentAcc.withdraw(amount);
            recordingTransactions("Withdrawal", -amount);
            System.out.println("Withdrawal successful. Remaining balance: $" + currentAcc.gettingBalance());
        } else {
            System.out.println("Invalid amount or insufficient balance.");
        }
    }


    private static void performingDeposit(Scanner scanner) {
        System.out.print("Enter the amount you want to deposit: ");
        double amount = scanner.nextDouble();
    
        if (amount > 0) {
            currentAcc.deposit(amount);
            recordingTransactions("Deposit", amount);
            System.out.println("Deposit successful. Updated balance: $" + currentAcc.gettingBalance());
        } else {
            System.out.println("Invalid amount. Please enter a positive value.");
        }
    }
    

    private static void performingTransfer(Scanner scanner) {
        System.out.print("Enter the amount you wish to transfer: ");
        double amount = scanner.nextDouble();
    
        if (amount > 0 && amount <= currentAcc.gettingBalance()) {
            System.out.print("Enter the reciever's account number: ");
            String recieverAccNo = scanner.next();
    
            Account reciever = findingAccbyAccNo(recieverAccNo);
            while (reciever == null) {
                System.out.println("Reciever's account not found. Please enter a valid reciever account number: ");
                recieverAccNo = scanner.next();
                reciever = findingAccbyAccNo(recieverAccNo);
            }
    
            currentAcc.withdraw(amount);
            reciever.deposit(amount);
    
            recordingTransactions("Transfer to " + recieverAccNo, -amount);
            System.out.println("Transfer successful. Remaining balance: $" + currentAcc.gettingBalance());
        } else {
            System.out.println("Insufficient balance.");
        }
    }
    

    private static void displayingAccDetails() {
        System.out.println("\nAccount Details");
        System.out.println("Account Number: " + currentAcc.gettingAccNo());
        System.out.println("Balance: $" + currentAcc.gettingBalance());
    }

    private static Account findingAccbyAccNo(String AccNo) {
        for (Account acc : accs) {
            if (acc.gettingAccNo().equals(AccNo)) {
                return acc;
            }
        }
        return null;
    }
}

class Account {
    private String AccNo;
    private String PinNo;
    private double balance;

    public Account(String AccNo, String PinNo, double balance) {
        this.AccNo = AccNo;
        this.PinNo = PinNo;
        this.balance = balance;
    }

    public String gettingAccNo() {
        return AccNo;
    }

    public String gettingPinNo() {
        return PinNo;
    }

    public double gettingBalance() {
        return balance;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public void deposit(double amount) {
        balance += amount;
    }
}

class Transaction {
    private String type;
    private double amount;
    private Date date;

    public Transaction(String type, double amount, Date date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }
}
