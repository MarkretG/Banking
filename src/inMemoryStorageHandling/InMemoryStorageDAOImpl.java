package inMemoryStorageHandling;

import bankingManagement.Account;
import bankingManagement.Customer;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryStorageDAOImpl implements InMemoryStorageDAO {
    private HashMap<Long,String> customerHashMap=new HashMap<>();
    private HashMap<Long,HashMap<Long,Account>> accountInfoHashMap=new HashMap<>();

    public void storeCustomersInCustomerHashMap(ArrayList<Customer> customers) {
        for (Customer customer:customers)
        {
            customerHashMap.put(customer.getCustomer_id(),customer.getName());
        }
    }

    @Override
    public void storeAccountsInAccountHashMap(ArrayList<Account> accounts) {
        for (Account account:accounts)
        {
            HashMap<Long,Account> accountHashMap = accountInfoHashMap.getOrDefault(account.getCustomer_id(), new HashMap<>());
            accountHashMap.put(account.getAccount_id(), account);
            accountInfoHashMap.put(account.getCustomer_id(), accountHashMap);
        }

    }
    @Override
    public void storeAccountInAccountHashMap(Account account) {
        HashMap<Long,Account> accountHashMap = accountInfoHashMap.getOrDefault(account.getCustomer_id(), new HashMap<>());
        accountHashMap.put(account.getAccount_id(), account);

        accountInfoHashMap.put(account.getCustomer_id(), accountHashMap);
    }
    @Override
    public HashMap<Long, Account> getAccountsInfo(long customer_id)throws AccountNotFoundException{
        HashMap<Long,Account> account=accountInfoHashMap.get(customer_id);
        if (account==null)
        {
            throw new AccountNotFoundException(customer_id);
        }
      return account;
    }

    @Override
    public HashMap<Long, String> getCustomerHashMap() {
        return customerHashMap;
    }

    @Override
    public HashMap<Long, HashMap<Long, Account>> getAccountHashMap() {
        return accountInfoHashMap;
    }
}
