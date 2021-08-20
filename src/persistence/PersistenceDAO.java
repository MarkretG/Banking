package persistence;
import bankingManagement.Account;
import bankingManagement.Customer;
import java.util.List;
import java.util.Map;

public interface PersistenceDAO{
    //insert  customers
    List<Long> addCustomers(List<Customer> customers) throws PersistenceException;

    //select customers
    List<Customer> getCustomers(List<Long> customer_ids) throws  PersistenceException;
    List<Customer> getAllCustomers()throws PersistenceException;

    //insert  account
    long addAccount(long customer_id,double balance) throws  PersistenceException;
    List<Long> addAccounts(Map<Long, Account> account)throws  PersistenceException;

    //select accounts by id
    List<Account> getAccounts(List<Long> customer_ids) throws  PersistenceException;
    Account getAccount(long customer_id) throws  PersistenceException;

    //select All Accounts
    List<Account> getAllAccounts()throws  PersistenceException;

    void updateAccount(long account_id ,double balance) throws PersistenceException;

    void removeAccount(long account_id) throws  PersistenceException;

    void deactivateCustomer(long customer_id) throws PersistenceException;

    void activateCustomer(long customer_id) throws PersistenceException;

    void cleanUp();
}
