package inMemoryStorageHandling;

public class AccountNotFoundException extends Exception {
    long customer_id;
    public  AccountNotFoundException(long customer_id)
    {
        this.customer_id=customer_id;
    }

    public long getCustomer_id() {
        return customer_id;
    }
}
