package logicalLayer;
import bankingManagement.Account;
import bankingManagement.Customer;
import inMemoryStorageHandling.AccountNotFoundException;
import persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum LogicalHandler {
    INSTANCE;

     LogicalHandler(){
        //initially store customer table and account table in hashmap
        try {
            //get all customers and store in customer HashMap
            ArrayList<Customer> customers = Controller.getPersistenceDAOHandler().getAllCustomers();
            Controller.getInMemoryStorageDAOHandler().storeCustomersInCustomerHashMap(customers);

            //get all Accounts and store in Hashmap account HashMap
            ArrayList<Account> accounts = Controller.getPersistenceDAOHandler().getAllAccounts();
            Controller.getInMemoryStorageDAOHandler().storeAccountsInAccountHashMap(accounts);
        } catch (PersistenceException  e) {
            e.printStackTrace();
            System.out.println("ERROR CODE:"+e.getErrorCode() + " " + e.getMessage()+" "+e.getCause());
        }
        catch (LogicalException e)
        {
            e.printStackTrace();
            System.out.println("ERROR CODE:"+e.getErrorCode() + " " + e.getMessage()+" "+e.getCause());
        }

    }
    public void handleNewCustomer(ArrayList<Customer> customers, ArrayList<Account> accounts){
          if (customers.isEmpty()|| accounts.isEmpty())
          {
            System.out.println("Handle new customers:"+"customers or accounts list is empty");
           }
        try {
            //insert customers in customer table and get generated customer ids
            ArrayList<Long> customer_ids = Controller.getPersistenceDAOHandler().addCustomers(customers);

            //map the inserted customer id and matching account info
            HashMap<Long, Account> account = getAccounts(customer_ids, accounts);

            //insert accounts in account table and get inserted customer ids
            ArrayList<Long> customer_id = Controller.getPersistenceDAOHandler().addAccounts(account);

            //get all inserted customers
            ArrayList<Customer> customers1 = Controller.getPersistenceDAOHandler().getCustomers(customer_ids);

            //store in customer HashMap
            Controller.getInMemoryStorageDAOHandler().storeCustomersInCustomerHashMap(customers1);

            // get all inserted Accounts
            ArrayList<Account> accounts1 = Controller.getPersistenceDAOHandler().getAccounts(customer_id);

            //store in account HashMap
            Controller.getInMemoryStorageDAOHandler().storeAccountsInAccountHashMap(accounts1);
        } catch (PersistenceException e) {
            e.printStackTrace();
            System.out.println("ERROR CODE:"+e.getErrorCode() + ":" + e.getMessage()+" "+e.getCause());
        }
        catch (LogicalException e)
        {
            e.printStackTrace();
            System.out.println("ERROR CODE:"+e.getErrorCode() + " " + e.getMessage()+" "+e.getCause());
        }
      }
    public void addNewAccountForExistingCustomer(long customer_id,double balance){
        if (customer_id<=0||balance<=0.0)
        {
            System.out.println("Add new account for existing customer:"+"customer id or balance is  less than zero");
        }
        try {
            //add new account for existing customer
             long account_id=Controller.getPersistenceDAOHandler().addAccount(customer_id, balance);

             Account account=getAccountObject(customer_id,account_id,balance);

            //store in account hashMap
             Controller.getInMemoryStorageDAOHandler().storeAccountInAccountHashMap(account);
        }
        catch (PersistenceException e) {
            e.printStackTrace();
            System.out.println("ERROR CODE:"+e.getErrorCode() + " " + e.getMessage()+" "+e.getCause());
        }
        catch (LogicalException e)
        {
            e.printStackTrace();
            System.out.println("ERROR CODE:"+e.getErrorCode() + " " + e.getMessage()+" "+e.getCause());
        }

    }
    public Customer getCustomerObject(String name,int age,long phone)
    {
        //create customer object
        Customer customer = new Customer();
        //customer.setCustomer_id(customer_id);
        customer.setName(name);
        customer.setAge(age);
        customer.setPhone(phone);
        return customer;
    }
    public Account getAccountObject(double balance)
    {
        //create account object
        Account account=new Account();
        account.setBalance(balance);
        return account;
    }

    public Account getAccountObject(long customer_id,long account_id,double balance)
    {
        //create account object
        Account account=new Account();
        account.setCustomer_id(customer_id);
        account.setAccount_id(account_id);
        account.setBalance(balance);
        return account;
    }
    public HashMap<Long,Account> getAccounts(ArrayList<Long> customer_ids,ArrayList<Account> accounts)
    {
        HashMap<Long,Account> account = new HashMap<>();

        for (int i = 0; i < customer_ids.size(); i++) {
            account.put(customer_ids.get(i), accounts.get(i));
        }
        return account;

    }

    public void deleteAccount(long customerId,long accountId) throws LogicalException, PersistenceException, AccountNotFoundException {
        if (customerId<=0||accountId<=0)
        {
            System.out.println("delete account:"+"account id or customer id is zero");
        }
        Controller.getPersistenceDAOHandler().deleteAccount(accountId);
       HashMap<Long,Account> accountHashMap=Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customerId);
       for (Map.Entry<Long,Account> entry: accountHashMap.entrySet())
       {
           if(entry.getKey()==accountId)
           {
               accountHashMap.remove(entry.getKey(),entry.getValue());
               break;
           }
       }
    }
    public void transactionOfWithdrawORDeposit(long customer_id,long account_id,double balance) throws LogicalException, PersistenceException, AccountNotFoundException {
       Controller.getPersistenceDAOHandler().updateAccount(account_id,balance);
        HashMap<Long,Account> accountHashMap=Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customer_id);
        for (Map.Entry<Long,Account> entry: accountHashMap.entrySet())
        {
            if(entry.getKey()==account_id)
            {
               entry.getValue().setBalance(balance);
               break;
            }
        }

    }
    public void closeConnection() throws LogicalException {
        Controller.getPersistenceDAOHandler().cleanUp();
    }


}
