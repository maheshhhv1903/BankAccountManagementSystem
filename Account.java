import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String accountId;
    private final String ownerName;
    private BigDecimal balance;
    private final String accountType;
    private final LocalDateTime createdAt;
    private final List<Transaction> transactions;

    public Account(String ownerName, String accountType, BigDecimal initialDeposit) {
        this.accountId = UUID.randomUUID().toString();
        this.ownerName = ownerName;
        this.accountType = accountType;
        this.createdAt = LocalDateTime.now();
        this.balance = initialDeposit == null ? BigDecimal.ZERO : initialDeposit;
        this.transactions = new ArrayList<>();
    }

    public synchronized void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public synchronized void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getAccountType() {
        return accountType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public synchronized void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public synchronized List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
