package ioHandler;
import bankingManagement.Account;
import bankingManagement.Customer;
import logicalLayer.LogicalHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public enum InputHandler {
    INSTANCE;
    private Scanner scanner=new Scanner(System.in);
    long customer_id;
    long account_id;
    int choice;
    double balance;
    public List<Customer> getCustomersInfo() {
        List<Customer> customers=new ArrayList<>();
        System.out.println("How many customers");
        int customersCount=scanner.nextInt();
        for(int i=0;i<customersCount;i++)
        {
            String name = validateName();
            int age=validateAge();
            long phone = validatePhone();
            Customer customer=LogicalHandler.INSTANCE.getCustomerObject(name,age,phone);
            customers.add(customer);
        }
        if(customers.isEmpty()){
            getCustomersInfo();
        }
        return customers;

    }
    public List<Account> getAccountsInfo(int accountsCounts) {
           List<Account> accounts=new ArrayList<>();
        for (int i=0;i<accountsCounts;i++) {
            System.out.println("Enter balance");
            double balance = getBalance();
            Account account = LogicalHandler.INSTANCE.getAccountObject(balance);
            accounts.add(account);
        }
        if(accounts.isEmpty())
        {
            getAccountsInfo(accountsCounts);
        }
        return accounts;
    }

    public long getCustomerId() {
        System.out.println("Enter customer_id");
        customer_id=scanner.nextLong();
        while (customer_id<=0)
        {
            System.out.println("Invalid customer_id");
            System.out.println("Please enter customer_id");
            customer_id=scanner.nextLong();
        }
        return customer_id;
    }

    public long getAccountId() {
        System.out.println("Enter account_id");
        account_id=scanner.nextLong();
        while (account_id<=0)
        {
            System.out.println("Invalid account_id");
            System.out.println("Please enter account_id");
            account_id=scanner.nextLong();
        }
        return account_id;
    }

    public int getChoice() {
        choice=scanner.nextInt();
        return choice;
    }
    public Double getBalance()
    {
        balance=scanner.nextDouble();
        while (balance<=0)
        {
            System.out.println("Balance should be positive");
            System.out.println("Enter balance");
            balance=scanner.nextDouble();
        }
        return balance;
    }
    public void closeScanner() {
        scanner.close();
    }
    private int validateAge() {
        System.out.println("Enter age");
        int age=scanner.nextInt();
        while ((age > 200) || (age < 5)) {
            System.out.println("Please enter a valid age");
            System.out.println("What is your age?\n");
            age = scanner.nextInt();
        }
        return age;
    }
    private String validateName()
    {
        System.out.println("Enter name");
        scanner.nextLine();
        String name=scanner.nextLine();
        while (name==null)
        {
            System.out.println("Name is null");
            System.out.println("Enter name");
            name=scanner.nextLine();
        }
        return name;
    }
    private long validatePhone()
    {
        System.out.println("Enter phone number");
        long phone=scanner.nextLong();
        while (phone<=0)
        {
            System.out.println("Invalid phone number");
            System.out.println("Please enter phone number");
            phone= scanner.nextLong();
        }
        return phone;
    }
   /* public String validateName(String name) {
        while (!Pattern.matches("^[a-zA-Z]\\W{2,50}", name)) {
            System.out.println("input mismatch");
            System.out.println("enter your name");
            scanner.nextLine();
            name = scanner.nextLine();
        }
    }

    */
}
