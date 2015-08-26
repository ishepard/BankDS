/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsfbeans;

import connector.RemoteConnector;
import ejbbeansremote.AdminBeanRemote;
import ejbbeansremote.BankTransferRemote;
import ejbbeansremote.BankTransactionRemote;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

/**
 *
 * @author shepard
 */
@ManagedBean
@SessionScoped
public class SessionScopedUser {
    RemoteConnector rc;
    AdminBeanRemote admin;
    BankTransferRemote transfer;
    BankTransactionRemote transaction;
    
    private boolean islogged = false;
    private String startingaccount;
    
    Long id;
    String firstname;
    String secondname;
    String username;
    String password;
    Long checkingaccount;
    Long creditcard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCheckingaccount() {
        return checkingaccount;
    }

    public void setCheckingaccount(Long checkingaccount) {
        this.checkingaccount = checkingaccount;
    }

    public Long getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(Long creditcard) {
        this.creditcard = creditcard;
    }

    public boolean isIslogged() {
        return islogged;
    }

    public void setIslogged(boolean islogged) {
        this.islogged = islogged;
    }

    public String getStartingaccount() {
        return startingaccount;
    }

    public void setStartingaccount(String startingaccount) {
        this.startingaccount = startingaccount;
    }
    
    /**
     * Creates a new instance of SessionScopedUser
     */
    public SessionScopedUser() {
        rc = new RemoteConnector("BankDS", "BankDS-ejb");
        System.out.println("Creating new SessionScoped bean");
        System.out.println("Obtained Remote accessor: " + rc.toString());
        try {
            admin = rc.lookup("AdminBean", AdminBeanRemote.class.getName());
            transfer = rc.lookup("BankTransfer", BankTransferRemote.class.getName());
            transaction = rc.lookup("BankTransaction", BankTransactionRemote.class.getName());
        } catch (NamingException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public String login(){
        System.out.println("User asked a login. Username is " + username + " and password is " + password);
        String pwd = admin.findUserByUsername(username);
        if (pwd != null){
            if (!pwd.equals(password)){
                System.out.println("The password specified is not correct");
                printMessage(FacesMessage.SEVERITY_ERROR, "Login Failed! The password specified is not correct.");
                return null;
            }
            completeUser();
            islogged = true;
            return "home";
        } else {
            // User does not exist
            System.out.println("User " + username + " does not exist");
            printMessage(FacesMessage.SEVERITY_ERROR, "Login Failed! User does not exist.");
            return null;
        }
    }
    
    public String logout(){
        //invalidate user session
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
        deleteUser();
        islogged = false;
        return "login?faces-redirect=true";
    }
    
    public void completeUser(){
        List user = admin.getUserEntity(username);
        id = Long.parseLong(user.get(0).toString());
        firstname = (String) user.get(1);
        secondname = (String) user.get(2);
        username = (String) user.get(3);
        password = (String) user.get(4);
        checkingaccount = Long.parseLong(user.get(5).toString());
        creditcard = Long.parseLong(user.get(6).toString());
    }
    
    public void deleteUser(){
        id = null;
        firstname = null;
        secondname = null;
        username = null;
        password = null;
        checkingaccount = null;
        creditcard = null;
    }
    
    public String insertBeneficiary(String username){
        System.out.println("New bank transaction, beneficiary is " + username);
        if (this.username.equals(username)){
            printMessage(FacesMessage.SEVERITY_ERROR, "You can not transfer money to yourself.");
            return null;
        } else if (!transaction.setBeneficiary(username)){
            printMessage(FacesMessage.SEVERITY_ERROR, "User not found, please check the value!");
            return null;
        } else 
            return "insertamount";
    }
    
    public List<String> getBeneficiary(){
        return transaction.getBeneficiary();
    }
    
    public String insertAmount(String amount){
        if (!checkCorrectnessAmount(amount)){
            return null;
        } else {
            if (transaction.setAmount(Long.parseLong(amount), username)){
                return "validation";
            } else {
                printMessage(FacesMessage.SEVERITY_ERROR, "You don't have enough money. Insert a number < " + checkingaccount);
                return null;
            }
        }
    }
    
    public String getAmount(){
        return transaction.getAmount();
    }
    
    public String confirmTransaction(String username, String password){
        try{
            checkingaccount = Long.parseLong(transaction.confirmTransaction(username, password));
            return "confirmationpage";
        } catch (Exception e){
            printMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
            return null;
        }
    }
    
    public String withdraw(String amount){
        System.out.println("Requested withdraw of " + amount + " from " + startingaccount);
        try{
            if (checkCorrectnessAmount(amount)){
                if (startingaccount.equals("checkingaccount")){
                    checkingaccount = Long.parseLong(transfer.withdraw(Long.parseLong(amount), startingaccount, username));
                } else 
                    creditcard = Long.parseLong(transfer.withdraw(Long.parseLong(amount), startingaccount, username));
                System.out.println("Withdraw completed successfully!");
                return "confirmationpage";
            } else
                return null;
        } catch (Exception ex){
            printMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            return null;
        }
    }
    
    public String refill(String amount){
        System.out.println("Requested refill of " + amount);
        if (checkCorrectnessAmount(amount)){
            if (startingaccount.equals("checkingaccount")){
                checkingaccount = Long.parseLong(transfer.refill(Long.parseLong(amount), startingaccount, username));
            } else 
                creditcard = Long.parseLong(transfer.refill(Long.parseLong(amount), startingaccount, username));
            return "confirmationpage";
        } else
            return null;
    }
    
    public boolean checkCorrectnessAmount(String amount){
        try{
            if (Long.parseLong(amount) <= 0){
                throw new java.lang.NumberFormatException();
            }
            return true;
        } catch (java.lang.NumberFormatException ex){
            printMessage(FacesMessage.SEVERITY_ERROR, "You have to insert a number > 0");
            return false;
        }
    }
    
    public void printMessage(FacesMessage.Severity severity, String message){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, message, null));
    }
    
}
