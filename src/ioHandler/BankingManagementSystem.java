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
        LogicalHandler.getInstance().initialiseHashMap();
        InputHandler inputHandler=InputHandler.getInstance();
        while (true)
        {
            System.out.println("1.New Customer");
            System.out.println("2.Add new account for existing customer");
            System.out.println("3.get accounts info for given customer_id");
            System.out.println("4.delete account");
            System.out.println("5.withdraw");
            System.out.println("6.deposit");
            //System.out.println("7.transfer money");
            System.out.println("8.exit");
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
                    long customer_id =validateCustomerId(inputHandler.getCustomerId());
                    long account_id = validateAccountId(customer_id, inputHandler.getAccountId());

                    LogicalHandler.getInstance().deleteAccount(customer_id, account_id);

                }
                break;
                case 5: {
                    long customer_id = validateCustomerId(inputHandler.getCustomerId());

                    long account_id = validateAccountId(customer_id,inputHandler.getAccountId());

                    double amount = validateWithdrawAmount(customer_id, account_id);

                    System.out.println("available amount:"+amount);

                    LogicalHandler.getInstance().transactionOfWithdrawORDeposit(customer_id, account_id, amount);
                    }
                    break;
                case 6:
                {
                    long customer_id= validateCustomerId(inputHandler.getCustomerId());

                    long account_id = validateAccountId(customer_id,inputHandler.getAccountId());

                    double amount=validateDepositAmount(customer_id,account_id);

                    System.out.println("available amount:"+amount);

                    LogicalHandler.getInstance().transactionOfWithdrawORDeposit(customer_id,account_id,amount);
                }
                break;
                /*case 7:
                    long customer_id= validateCustomerId(inputHandler.getCustomerId());

                    long account_id = validateAccountId(customer_id,inputHandler.getAccountId());

                 */

                case 7:
                    DBUtil.closeConnection();
                    inputHandler.closeScanner();
                    System.exit(0);
            }

        }


    }
    public static double validateWithdrawAmount(long customerId,long account_id) throws LogicalException, AccountNotFoundException {
        InputHandler inputHandler=InputHandler.getInstance();
        System.out.println("enter withdraw amount");
        double withdrawalAmount= inputHandler.getBalance();
        HashMap<Long,Account> accountHashMap= Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customerId);
            for (Map.Entry<Long, Account> entry : accountHashMap.entrySet()) {
                if (entry.getKey() == account_id) {
                    System.out.println("savings amount:"+entry.getValue().getBalance());
                    while ((entry.getValue().getBalance() < withdrawalAmount || withdrawalAmount <= 0)) {
                        System.out.println("withdraw amount does not exceed savings amount or withdraw amount should be positive");
                        withdrawalAmount = inputHandler.getBalance();
                    }
                    System.out.println("with draw amount:"+withdrawalAmount);
                    withdrawalAmount = entry.getValue().getBalance() - withdrawalAmount;
                    break;
                }
            }
            return withdrawalAmount;
      }

    public static double validateDepositAmount(long customerId,long account_id) throws LogicalException, AccountNotFoundException {
        InputHandler inputHandler = InputHandler.getInstance();
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
    public static long  validateCustomerId(long customer_id) throws LogicalException {
        HashMap<Long,HashMap<Long,Account>> account=Controller.getInMemoryStorageDAOHandler().getAccountHashMap();
        if (account.containsKey(customer_id)) {
            return customer_id;
        }
        else
        {
            while (!account.containsKey(customer_id)) {
                System.out.println("enter customer_id is not available");
                customer_id = InputHandler.getInstance().getCustomerId();
            }
        }
        return customer_id;
    }
    public static long  validateAccountId(long customer_id,long account_id) throws LogicalException, AccountNotFoundException {
        HashMap<Long,Account> accountHashMap=Controller.getInMemoryStorageDAOHandler().getAccountsInfo(customer_id);
        if (accountHashMap.containsKey(account_id)) {
            return account_id;
        }

        while (!accountHashMap.containsKey(account_id)) {
            System.out.println("enter account id  is mismatch for given customer_id");
            account_id = InputHandler.getInstance().getAccountId();
        }

        return account_id;
    }
}





