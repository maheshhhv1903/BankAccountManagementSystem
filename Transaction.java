import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type { DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT }

    private final String id;
    private final LocalDateTime time;
    private final Type type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;
    private final String note;

    public Transaction(Type type, BigDecimal amount, BigDecimal balanceAfter, String note) {
        this.id = UUID.randomUUID().toString();
        this.time = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.note = note;
    }

    public String getId() { return id; }
    public LocalDateTime getTime() { return time; }
    public Type getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public String getNote() { return note; }

    @Override
    public String toString() {
        return "[" + time + "] " + type + " " + amount + " -> balance: " + balanceAfter + " (" + note + ")";
    }
}
