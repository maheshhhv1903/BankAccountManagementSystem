import java.math.BigDecimal;
import java.util.*;

public class BankService {
    private final Map<String, Account> accounts;
    private final BankDAO dao;

    public BankService(BankDAO dao) {
        this.dao = dao;
        this.accounts = dao.load();
    }

    public synchronized Account createAccount(String ownerName, String accountType, BigDecimal initialDeposit) {
        Account acc = new Account(ownerName, accountType, initialDeposit);
        if (initialDeposit != null && initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
            Transaction t = new Transaction(Transaction.Type.DEPOSIT, initialDeposit, initialDeposit, "Initial deposit");
            acc.addTransaction(t);
        }
        accounts.put(acc.getAccountId(), acc);
        dao.save(accounts);
        return acc;
    }

    public synchronized List<Account> listAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public synchronized Account getAccount(String id) {
        return accounts.get(id);
    }

    public synchronized boolean deposit(String accountId, BigDecimal amount, String note) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        Account acc = accounts.get(accountId);
        if (acc == null) return false;
        acc.deposit(amount);
        Transaction t = new Transaction(Transaction.Type.DEPOSIT, amount, acc.getBalance(), note);
        acc.addTransaction(t);
        dao.save(accounts);
        return true;
    }

    public synchronized boolean withdraw(String accountId, BigDecimal amount, String note) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        Account acc = accounts.get(accountId);
        if (acc == null) return false;
        if (acc.getBalance().compareTo(amount) < 0) return false;
        acc.withdraw(amount);
        Transaction t = new Transaction(Transaction.Type.WITHDRAW, amount, acc.getBalance(), note);
        acc.addTransaction(t);
        dao.save(accounts);
        return true;
    }

    public synchronized boolean transfer(String fromId, String toId, BigDecimal amount, String note) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);
        if (from == null || to == null) return false;
        if (from.getBalance().compareTo(amount) < 0) return false;

        // perform transfer
        from.withdraw(amount);
        to.deposit(amount);

        Transaction tOut = new Transaction(Transaction.Type.TRANSFER_OUT, amount, from.getBalance(), "To " + toId + " - " + note);
        Transaction tIn = new Transaction(Transaction.Type.TRANSFER_IN, amount, to.getBalance(), "From " + fromId + " - " + note);
        from.addTransaction(tOut);
        to.addTransaction(tIn);

        dao.save(accounts);
        return true;
    }
}
