import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DATA_FILE = "bankdata.ser";

    public static void main(String[] args) {
        BankDAO dao = new BankDAO(DATA_FILE);
        BankService service = new BankService(dao);

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    createAccount(service);
                    break;
                case "2":
                    listAccounts(service);
                    break;
                case "3":
                    deposit(service);
                    break;
                case "4":
                    withdraw(service);
                    break;
                case "5":
                    transfer(service);
                    break;
                case "6":
                    showTransactions(service);
                    break;
                case "0":
                    System.out.println("Exiting. Goodbye.");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== BANK MENU ===");
        System.out.println("1. Create account");
        System.out.println("2. List accounts");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Transfer");
        System.out.println("6. Show transaction history");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void createAccount(BankService service) {
        System.out.print("Owner name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name required.");
            return;
        }
        System.out.print("Account type (SAVINGS/CURRENT): ");
        String type = scanner.nextLine().trim();
        if (type.isEmpty()) type = "SAVINGS";
        System.out.print("Initial deposit (numeric, 0 allowed): ");
        String depStr = scanner.nextLine().trim();
        BigDecimal deposit = parseAmount(depStr);
        if (deposit == null) return;
        Account a = service.createAccount(name, type, deposit);
        System.out.println("Created account: " + a.getAccountId());
    }

    private static void listAccounts(BankService service) {
        List<Account> accounts = service.listAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts.");
            return;
        }
        for (Account a : accounts) {
            System.out.println(a);
        }
    }

    private static void deposit(BankService service) {
        System.out.print("Account ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Amount: ");
        BigDecimal amt = parseAmount(scanner.nextLine().trim());
        if (amt == null) return;
        System.out.print("Note: ");
        String note = scanner.nextLine().trim();
        boolean ok = service.deposit(id, amt, note);
        System.out.println(ok ? "Deposit successful." : "Deposit failed. Check account ID.");
    }

    private static void withdraw(BankService service) {
        System.out.print("Account ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Amount: ");
        BigDecimal amt = parseAmount(scanner.nextLine().trim());
        if (amt == null) return;
        System.out.print("Note: ");
        String note = scanner.nextLine().trim();
        boolean ok = service.withdraw(id, amt, note);
        System.out.println(ok ? "Withdraw successful." : "Withdraw failed. Check balance/ID.");
    }

    private static void transfer(BankService service) {
        System.out.print("From Account ID: ");
        String from = scanner.nextLine().trim();
        System.out.print("To Account ID: ");
        String to = scanner.nextLine().trim();
        System.out.print("Amount: ");
        BigDecimal amt = parseAmount(scanner.nextLine().trim());
        if (amt == null) return;
        System.out.print("Note: ");
        String note = scanner.nextLine().trim();
        boolean ok = service.transfer(from, to, amt, note);
        System.out.println(ok ? "Transfer successful." : "Transfer failed. Check accounts/balance.");
    }

    private static void showTransactions(BankService service) {
        System.out.print("Account ID: ");
        String id = scanner.nextLine().trim();
        Account a = service.getAccount(id);
        if (a == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.println("Transactions for account " + id);
        for (Transaction t : a.getTransactions()) {
            System.out.println(t);
        }
    }

    private static BigDecimal parseAmount(String s) {
        try {
            BigDecimal bd = new BigDecimal(s);
            if (bd.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Amount must be non-negative.");
                return null;
            }
            return bd;
        } catch (Exception e) {
            System.out.println("Invalid amount.");
            return null;
        }
    }
}
