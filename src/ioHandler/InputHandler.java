package ioHandler;
import bankingManagement.Account;
import bankingManagement.Customer;
import logicalLayer.LogicalHandler;
import java.util.ArrayList;
import java.util.Scanner;
public enum InputHandler {
    INSTANCE;
    private Scanner scanner=new Scanner(System.in);
    long customer_id;
    long account_id;
    int choice;
    double balance;
    public ArrayList<Customer> getCustomersInfo() {
        ArrayList<Customer> customers=new ArrayList<>();
        System.out.println("how many customers");
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
    public ArrayList<Account> getAccountsInfo(int accountsCounts) {
        ArrayList<Account> accounts=new ArrayList<>();
        for (int i=0;i<accountsCounts;i++) {
            System.out.println("enter balance");
            double balance = scanner.nextDouble();
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
        System.out.println("enter customer_id");
        customer_id=scanner.nextLong();
        while (customer_id<=0)
        {
            System.out.println("invalid customer_id");
            System.out.println("please enter customer_id");
            customer_id=scanner.nextLong();
        }
        return customer_id;
    }

    public long getAccountId() {
        System.out.println("enter account_id");
        account_id=scanner.nextLong();
        while (account_id<=0)
        {
            System.out.println("invalid account_id");
            System.out.println("please enter account_id");
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
        System.out.println("enter balance");
        balance=scanner.nextDouble();
        while (balance<=0)
        {
            System.out.println("balance should be positive");
            System.out.println("enter balance");
            balance=scanner.nextDouble();
        }
        return balance;
    }
    public void closeScanner() {
        scanner.close();
    }
    private int validateAge() {
        System.out.println("enter age");
        int age=scanner.nextInt();
        while ((age > 200) || (age < 5)) {
            System.out.println("ERROR Please enter a valid age");
            System.out.println("What is your age?\n");
            age = scanner.nextInt();
        }
        return age;
    }
    private String validateName()
    {
        System.out.println("enter name");
        String name=scanner.nextLine();
        while (name==null)
        {
            System.out.println("name is null");
            System.out.println("enter name");
            name=scanner.nextLine();
        }
        return name;
    }
    private long validatePhone()
    {
        System.out.println("enter phone number");
        long phone=scanner.nextLong();
        while (phone<=0)
        {
            System.out.println("invalid phone number");
            System.out.println("please enter phone number");
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
