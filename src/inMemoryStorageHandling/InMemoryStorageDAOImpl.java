package inMemoryStorageHandling;
import bankingManagement.Account;
import bankingManagement.Customer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryStorageDAOImpl implements InMemoryStorageDAO {
    private Map<Long,String> customersMap=new HashMap<>();
    private Map<Long,Map<Long,Account>> accountsInfoMap=new HashMap<>();

    public void storeCustomersInCustomerMap(List<Customer> customers) {
        if (customers.isEmpty())
        {
            System.out.println("Store customer in customer hashmap:"+"customers list is empty");
        }
        for (Customer customer:customers)
        {
            customersMap.put(customer.getCustomer_id(),customer.getName());
        }
    }

    @Override
    public void storeAccountsInAccountMap(List<Account> accounts) {
        if (accounts.isEmpty())
        {
            System.out.println("store account in account hashmap:"+"accounts list is empty");
        }
        for (Account account:accounts)
        {
            Map<Long,Account> accountMap = accountsInfoMap.getOrDefault(account.getCustomer_id(), new HashMap<>());
            accountMap.put(account.getAccount_id(), account);
            accountsInfoMap.put(account.getCustomer_id(), accountMap);
        }

    }
    @Override
    public void storeAccountInAccountMap(Account account) {
        Map<Long,Account> accountMap = accountsInfoMap.getOrDefault(account.getCustomer_id(), new HashMap<>());
        accountMap.put(account.getAccount_id(), account);

        accountsInfoMap.put(account.getCustomer_id(), accountMap);
    }

    @Override
    public Map<Long, String> getCustomersMap() {
        return customersMap;
    }

    @Override
    public Map<Long, Map<Long, Account>> getAccountsMap() {
        return accountsInfoMap;
    }
}
