package inMemoryStorageHandling;

import bankingManagement.Account;
import bankingManagement.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InMemoryStorageDAO {
    //store all customers in customer hashmap
    void storeCustomersInCustomerMap(List<Customer> customers);

    //store all accounts in account hashmap
    void storeAccountsInAccountMap(List<Account> accounts);

    //store account in account hashmap
    void storeAccountInAccountMap(Account account);

    Map<Long, String> getCustomersMap();

    Map<Long,Map<Long,Account>> getAccountsMap();

}