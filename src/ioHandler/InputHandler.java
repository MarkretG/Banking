package ioHandler;

import bankingManagement.Account;
import bankingManagement.Customer;
import logicalLayer.Controller;
import logicalLayer.LogicalException;
import logicalLayer.LogicalHandler;

import java.util.ArrayList;
import java.util.Scanner;

public class InputHandler {

    private static InputHandler inputHandler = null;

    public static InputHandler getInstance() {
        if (inputHandler == null) {
            inputHandler = new InputHandler();
        }
        return inputHandler;
    }
    private Scanner scanner=new Scanner(System.in);
    long customerId;
    long accountId;
    int choice;
    double balance;
    public ArrayList<Customer> getCustomerInfo() {
        ArrayList<Customer> customers=new ArrayList<>();
        System.out.println("how many customers");
        int customersCount=scanner.nextInt();
        for(int i=0;i<customersCount;i++)
        {
            //System.out.println("Enter customer_id");
            //long customer_id = scanner.nextLong();
            System.out.println("enter name");
            scanner.nextLine();
            String name = scanner.nextLine();
            System.out.println("enter age");
            int age = scanner.nextInt();
            System.out.println("enter phone Number");
            long phone = scanner.nextLong();
            Customer customer=LogicalHandler.getInstance().getCustomerObject(name,age,phone);
            customers.add(customer);
        }
        return customers;

    }
    public ArrayList<Account> getAccountInfo(int accountsCounts) {
        ArrayList<Account> accounts=new ArrayList<>();
        //System.out.println("enter customer_id");
        //long customer_id = scanner.nextLong();
        //System.out.println("enter account_id");
        //long account_id = scanner.nextLong();
        for (int i=0;i<accountsCounts;i++) {
            System.out.println("enter balance");
            double balance = scanner.nextDouble();
            Account account = LogicalHandler.getInstance().getAccountObject(balance);
            accounts.add(account);
        }
        return accounts;
    }

    public long getCustomerId() {
        System.out.println("enter customer_id");
        customerId=scanner.nextLong();
        return customerId;
    }

    public long getAccountId() {
        System.out.println("enter account_id");
        accountId=scanner.nextLong();
        return accountId;
    }

    public int getChoice() {
        choice=scanner.nextInt();
        return choice;
    }
    public Double getBalance()
    {
        balance=scanner.nextDouble();
        return balance;
    }
    public void closeScanner() {
        scanner.close();
    }


}
