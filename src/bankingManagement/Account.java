package bankingManagement;
public class Account {
    private long customer_id;
    private long account_id;
    private  double balance;

    @Override
    public String toString() {
        return "Account{" +
                "customer_id=" + customer_id +
                ", account_id=" + account_id +
                ", balance=" + balance +
                '}';
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

