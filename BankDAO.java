import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BankDAO {
    private final String filename;

    public BankDAO(String filename) {
        this.filename = filename;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Account> load() {
        File f = new File(filename);
        if (!f.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            return (Map<String, Account>) obj;
        } catch (Exception e) {
            System.err.println("Failed to load data: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public void save(Map<String, Account> accounts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }
}
