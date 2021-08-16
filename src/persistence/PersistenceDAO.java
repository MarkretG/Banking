package persistence;
import bankingManagement.Account;
import bankingManagement.Customer;

import java.util.ArrayList;
import java.util.HashMap;

public interface PersistenceDAO{
    //insert  customers
    ArrayList<Long> addCustomers(ArrayList<Customer> customers) throws PersistenceException;

    //select customers
    ArrayList<Customer> selectCustomers(ArrayList<Long> customer_ids) throws  PersistenceException;
    ArrayList<Customer> selectAllCustomers()throws PersistenceException;

    //insert  account
    void addAccount(long customer_id,double balance) throws  PersistenceException;
    ArrayList<Long> addAccounts(HashMap<Long, Account> account)throws  PersistenceException;

    //select accounts by id
    ArrayList<Account> selectAccounts(ArrayList<Long> customer_ids) throws  PersistenceException;
    Account selectAccount(long customer_id) throws  PersistenceException;

    //select All Accounts
    ArrayList<Account> selectAllAccounts()throws  PersistenceException;

    void updateAccount(long account_id ,double balance) throws PersistenceException;

    void updateAccount(long customer_id,long account_id) throws  PersistenceException;

    void deleteAccount();


}
