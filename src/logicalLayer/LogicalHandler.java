package logicalLayer;
import bankingManagement.Account;
import bankingManagement.Customer;
import persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum LogicalHandler {
    INSTANCE;

     LogicalHandler(){
        //initially store customer table and account table in hashmap
        try {
            //get all customers and store in customer HashMap
            List<Customer> customers = Controller.getPersistenceDAOHandler().getAllCustomers();
            Controller.getInMemoryStorageDAOHandler().storeCustomersInCustomerMap(customers);

            //get all Accounts and store in Hashmap account HashMap
            List<Account> accounts = Controller.getPersistenceDAOHandler().getAllAccounts();
            Controller.getInMemoryStorageDAOHandler().storeAccountsInAccountMap(accounts);
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
    public void handleNewCustomer(List<Customer> customers, List<Account> accounts){
          if (customers.isEmpty()|| accounts.isEmpty())
          {
            System.out.println("Handle new customers:"+"customers or accounts list is empty");
           }
        try {
            //insert customers in customer table and get generated customer ids
            List<Long> customer_ids = Controller.getPersistenceDAOHandler().addCustomers(customers);

            //map the inserted customer id and matching account info
            Map<Long, Account> account = getAccounts(customer_ids, accounts);

            //insert accounts in account table and get inserted customer ids
            List<Long> customer_id = Controller.getPersistenceDAOHandler().addAccounts(account);

            //get all inserted customers
            List<Customer> customers1 = Controller.getPersistenceDAOHandler().getCustomers(customer_ids);

            //store in customer HashMap
            Controller.getInMemoryStorageDAOHandler().storeCustomersInCustomerMap(customers1);

            // get all inserted Accounts
            List<Account> accounts1 = Controller.getPersistenceDAOHandler().getAccounts(customer_id);

            //store in account HashMap
            Controller.getInMemoryStorageDAOHandler().storeAccountsInAccountMap(accounts1);
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

             Controller.getPersistenceDAOHandler().activateCustomer(customer_id);

             Account account=getAccountObject(customer_id,account_id,balance);

            //store in account hashMap
             Controller.getInMemoryStorageDAOHandler().storeAccountInAccountMap(account);
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
    public Map<Long,Account> getAccounts(List<Long> customer_ids,List<Account> accounts)
    {
        Map<Long,Account> account = new HashMap<>();

        for (int i = 0; i < customer_ids.size(); i++) {
            account.put(customer_ids.get(i), accounts.get(i));
        }
        return account;

    }

    public Map<Long, Account> getAccountsInfo(long customer_id) throws LogicalException {
        Map<Long,Map<Long,Account>> accountMap=Controller.getInMemoryStorageDAOHandler().getAccountsMap();
        Map<Long,Account>  accountInfoMap=accountMap.get(customer_id);
        if (accountInfoMap==null||accountInfoMap.isEmpty())
        {
            throw  new LogicalException("Account not found for given customer id",100);
        }
        return accountInfoMap;
    }

    public void deleteAccount(long customerId,long accountId) throws LogicalException, PersistenceException {
        if (customerId<=0||accountId<=0)
        {
            System.out.println("Delete account:"+"account id or customer id is zero");
        }

        Controller.getPersistenceDAOHandler().removeAccount(accountId);
        Map<Long,Account> accountMap=getAccountsInfo(customerId);
        if(accountMap.size()==1)
        {
            Controller.getPersistenceDAOHandler().deactivateCustomer(customerId);
        }
       for (Map.Entry<Long,Account> entry: accountMap.entrySet())
       {

           if(entry.getKey()==accountId)
           {
               accountMap.remove(entry.getKey(),entry.getValue());
               break;
           }
       }
    }
    public void transactionOfWithdrawORDeposit(long customer_id,long account_id,double balance) throws LogicalException, PersistenceException{
       Controller.getPersistenceDAOHandler().updateAccount(account_id,balance);
        Map<Long,Account> accountHashMap=getAccountsInfo(customer_id);
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
