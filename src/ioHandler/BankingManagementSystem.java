package ioHandler;
import bankingManagement.Account;
import bankingManagement.Customer;
import inMemoryStorageHandling.AccountNotFoundException;
import logicalLayer.Controller;
import logicalLayer.LogicalException;
import logicalLayer.LogicalHandler;
import persistence.PersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class  BankingManagementSystem {
    public static void main(String[] args) throws LogicalException, PersistenceException, AccountNotFoundException {
        LogicalHandler.INSTANCE.initialiseHashMap();
        while (true)
        {
            System.out.println("1.New Customer");
            System.out.println("2.Add new account for existing customer");
            System.out.println("3.get accounts info for given customer_id");
            System.out.println("4.delete account");
            System.out.println("5.withdraw");
            System.out.println("6.deposit");
            System.out.println("7.exit");
            int choice=InputHandler.INSTANCE.getChoice();
            switch (choice)
            {
                case 1: addNewCustomers();break;

                case 2: addNewAccount(); break;

                case 3: getAccountsInfo();break;

                case 4: deleteAccount();break;

                case 5: withDraw();break;

                case 6: deposit();break;

                case 7:
                    LogicalHandler.INSTANCE.CloseConnection();
                    InputHandler.INSTANCE.closeScanner();
                    System.exit(0);
            }

        }
    }

    private static double validateWithdrawAmount(long customerId,long account_id) throws LogicalException, AccountNotFoundException {
        System.out.println("enter withdraw amount");
        double withdrawalAmount= InputHandler.INSTANCE.getBalance();
        HashMap<Long,Account> accountHashMap= Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customerId);
            for (Map.Entry<Long, Account> entry : accountHashMap.entrySet()) {
                if (entry.getKey() == account_id) {
                    System.out.println("savings amount:"+entry.getValue().getBalance());
                    while ((entry.getValue().getBalance() < withdrawalAmount || withdrawalAmount <= 0)) {
                        System.out.println("withdraw amount does not exceed savings amount or withdraw amount should be positive");
                        withdrawalAmount = InputHandler.INSTANCE.getBalance();
                    }
                    System.out.println("with draw amount:"+withdrawalAmount);
                    break;
                }
            }
            return withdrawalAmount;
      }


    private static double validateDepositAmount(long customerId,long account_id) throws LogicalException, AccountNotFoundException {
        InputHandler inputHandler = InputHandler.INSTANCE;
        System.out.println("enter deposit amount");
        double balance = inputHandler.getBalance();
        HashMap<Long, Account> accountHashMap = Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customerId);
            for (Map.Entry<Long, Account> entry : accountHashMap.entrySet()) {
                if (entry.getKey() == account_id) {
                    System.out.println("savings amount:"+entry.getValue().getBalance());
                    while (balance <= 0) {
                        System.out.println("amount should be positive");
                        balance = inputHandler.getBalance();
                    }
                    System.out.println("deposit amount:"+balance);
                    balance = entry.getValue().getBalance() + balance;
                    break;
                }
          }
        return balance;
    }

    private static long  checkCustomerIdInAccountHashMap(long customer_id) throws LogicalException {
        HashMap<Long,HashMap<Long,Account>> account=Controller.getInMemoryStorageDAOHandler().getAccountHashMap();
        if (account.containsKey(customer_id)) {
            return customer_id;
        }
            while (!account.containsKey(customer_id)) {
                System.out.println("enter customer_id is not available");
                customer_id = InputHandler.INSTANCE.getCustomerId();
            }
        return customer_id;
    }

    public static long checkAccountIdIsMatchGivenCustomerId(long customer_id,long account_id) throws LogicalException, AccountNotFoundException {
        HashMap<Long,Account> accountHashMap=Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customer_id);
        if (accountHashMap.containsKey(account_id)) {
            return account_id;
        }

        while (!accountHashMap.containsKey(account_id)) {
            System.out.println("enter account id  is mismatch for given customer_id");
            account_id = InputHandler.INSTANCE.getAccountId();
        }
        return account_id;
    }

    private   static  double getSavingsAmount(long customer_id,long account_id) throws LogicalException, AccountNotFoundException {
        HashMap<Long, Account> accountHashMap = Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customer_id);
        for (Map.Entry<Long, Account> entry : accountHashMap.entrySet()) {
            if (entry.getKey() == account_id) {
                return entry.getValue().getBalance();
            }
        }
        return 0;
    }

    private static void withDraw() throws LogicalException, AccountNotFoundException, PersistenceException {
        long customer_id = checkCustomerIdInAccountHashMap(InputHandler.INSTANCE.getCustomerId());

        long account_id = checkAccountIdIsMatchGivenCustomerId(customer_id,InputHandler.INSTANCE.getAccountId());

        double withdrawalAmount = validateWithdrawAmount(customer_id, account_id);

        double savingsAmount=getSavingsAmount(customer_id,account_id);

        double updatedAmount=savingsAmount-withdrawalAmount;

        LogicalHandler.INSTANCE.transactionOfWithdrawORDeposit(customer_id, account_id,updatedAmount);
    }


    private static void deposit() throws LogicalException, AccountNotFoundException, PersistenceException {

        long customer_id= checkCustomerIdInAccountHashMap(InputHandler.INSTANCE.getCustomerId());

        long account_id = checkAccountIdIsMatchGivenCustomerId(customer_id,InputHandler.INSTANCE.getAccountId());

        double depositAmount=validateDepositAmount(customer_id,account_id);

        double savingsAmount=getSavingsAmount(customer_id,account_id);

        double updatedAmount=savingsAmount+depositAmount;

        LogicalHandler.INSTANCE.transactionOfWithdrawORDeposit(customer_id,account_id,updatedAmount);
    }

    private static void addNewCustomers()
    {
        ArrayList<Customer> customers = InputHandler.INSTANCE.getCustomersInfo();
        ArrayList<Account> accounts = InputHandler.INSTANCE.getAccountsInfo(customers.size());
        LogicalHandler.INSTANCE.handleNewCustomer(customers, accounts);
    }

    private  static void addNewAccount()
    {
        long customer_id = InputHandler.INSTANCE.getCustomerId();
        double balance = InputHandler.INSTANCE.getBalance();
        LogicalHandler.INSTANCE.addNewAccountForExistingCustomer(customer_id,balance);
    }

    private static void getAccountsInfo()
    {
        long customer_id = InputHandler.INSTANCE.getCustomerId();
        try {
            HashMap<Long, Account> accountInfo = Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customer_id);
            System.out.println(accountInfo);
        } catch (AccountNotFoundException | LogicalException e) {
            System.out.println("Customer_id:" + e.getMessage() + "customer_id not available in account table");
        }
    }

    private static void deleteAccount() throws LogicalException, PersistenceException, AccountNotFoundException {
        long customer_id =checkCustomerIdInAccountHashMap(InputHandler.INSTANCE.getCustomerId());
        long account_id = checkAccountIdIsMatchGivenCustomerId(customer_id, InputHandler.INSTANCE.getAccountId());

        LogicalHandler.INSTANCE.deleteAccount(customer_id, account_id);
    }


}





