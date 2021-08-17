package persistence;
import bankingManagement.Account;
import bankingManagement.Customer;

import java.util.ArrayList;
import java.util.HashMap;

public interface PersistenceDAO{
    //insert  customers
    ArrayList<Long> addCustomers(ArrayList<Customer> customers) throws PersistenceException;

    //select customers
    ArrayList<Customer> getCustomers(ArrayList<Long> customer_ids) throws  PersistenceException;
    ArrayList<Customer> getAllCustomers()throws PersistenceException;

    //insert  account
    void addAccount(long customer_id,double balance) throws  PersistenceException;
    ArrayList<Long> addAccounts(HashMap<Long, Account> account)throws  PersistenceException;

    //select accounts by id
    ArrayList<Account> getAccounts(ArrayList<Long> customer_ids) throws  PersistenceException;
    Account getAccount(long customer_id) throws  PersistenceException;

    //select All Accounts
    ArrayList<Account> getAllAccounts()throws  PersistenceException;

    void updateAccount(long account_id ,double balance) throws PersistenceException;

    void deleteAccount(long account_id) throws  PersistenceException;


}
