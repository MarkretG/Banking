package ioHandler;
import bankingManagement.Account;
import bankingManagement.Customer;
import inMemoryStorageHandling.AccountNotFoundException;
import logicalLayer.Controller;
import logicalLayer.LogicalException;
import logicalLayer.LogicalHandler;
import persistence.DBUtil;
import persistence.PersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  BankingManagementSystem {
    public static void main(String[] args) throws LogicalException, PersistenceException, AccountNotFoundException {
        InputHandler inputHandler=InputHandler.getInstance();
        while (true)
        {

            System.out.println("1.New Customer\n2.Add new account for existing customer\n3.get accounts info for given customer_id\n4.delete account\n5.withdraw\n6.deposit\n7.exit");
            int choice=inputHandler.getChoice();
            switch (choice)
            {
                case 1: {
                    ArrayList<Customer> customers = inputHandler.getCustomerInfo();
                    ArrayList<Account> accounts = inputHandler.getAccountInfo(customers.size());
                    LogicalHandler.getInstance().handleNewCustomer(customers, accounts);
                    }
                break;
                case 2: {
                    long customer_id = inputHandler.getCustomerId();
                    System.out.println("enter balance");
                    double balance = inputHandler.getBalance();
                    LogicalHandler.getInstance().addNewAccountForExistingCustomer(customer_id,balance);
                   }
                break;
                case 3: {
                    long customer_id = inputHandler.getCustomerId();
                    try {
                        HashMap<Long, Account> accountInfo = Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customer_id);
                        System.out.println(accountInfo);
                    } catch (AccountNotFoundException e) {
                        System.out.println("Customer_id:" + e.getCustomer_id() + "this customer_id not available in account table");
                    }
                }
                    break;
                case 4: {
                    long customer_id = inputHandler.getCustomerId();
                    long account_id = inputHandler.getAccountId();
                    LogicalHandler.getInstance().deleteAccount(customer_id, account_id);

                }
                break;
                case 5: {
                    long customer_id = inputHandler.getCustomerId();
                    System.out.println("enter account id");
                    long account_id = inputHandler.getAccountId();
                    double amount = validateWithdrawAmountAndAccountId(customer_id, account_id);
                    LogicalHandler.getInstance().transaction(customer_id, account_id, amount);
                   }
                    break;
                case 6:
                {
                    long customer_id= inputHandler.getCustomerId();
                    System.out.println("enter account id");
                    long account_id = inputHandler.getAccountId();
                    double amount=validateDepositAmountAndAccountId(customer_id,account_id);
                    LogicalHandler.getInstance().transaction(customer_id,account_id,amount);
                }


                case 7:
                    DBUtil.closeConnection();
                    inputHandler.closeScanner();
                    System.exit(0);
            }

        }


    }
    public static double validateWithdrawAmountAndAccountId(long customerId,long account_id) throws LogicalException, AccountNotFoundException {
        InputHandler inputHandler=InputHandler.getInstance();
        System.out.println("enter withdraw amount");
        double balance= inputHandler.getBalance();
        HashMap<Long,Account> accountHashMap= Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customerId);
        if(accountHashMap.containsKey(account_id)) {
            for (Map.Entry<Long, Account> entry : accountHashMap.entrySet()) {
                if (entry.getKey() == account_id) {
                    System.out.println("saving amount:" + entry.getValue().getBalance());
                    while ((entry.getValue().getBalance() < balance || balance <= 0)) {
                        System.out.println("withdraw amount does not exceed or withdraw amount should be positive re enter amount");
                        balance = inputHandler.getBalance();
                    }
                    balance = entry.getValue().getBalance() - balance;
                    return balance;
                }
            }
        }
        else
        {
            System.out.println("account id is not available");
        }
        return 0;
    }

    public static double validateDepositAmountAndAccountId(long customerId,long account_id) throws LogicalException, AccountNotFoundException {
        InputHandler inputHandler=InputHandler.getInstance();
        System.out.println("enter deposit amount");
        double balance= inputHandler.getBalance();
        HashMap<Long,Account> accountHashMap= Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customerId);
        if(accountHashMap.containsKey(account_id)) {
            for (Map.Entry<Long, Account> entry : accountHashMap.entrySet()) {
                if (entry.getKey() == account_id) {
                    System.out.println("saving amount:" + entry.getValue().getBalance());
                    while (balance <= 0) {
                        System.out.println("withdraw amount should be positive re enter amount");
                        balance = inputHandler.getBalance();
                    }
                    balance = entry.getValue().getBalance() + balance;
                    return balance;
                }
            }
        }
        else
        {
            System.out.println("account id is not available");
        }
        return 0;
    }
}





