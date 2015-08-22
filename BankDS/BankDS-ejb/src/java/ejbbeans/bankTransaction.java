/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbbeans;

import entities.UserEntity;
import exceptions.IncorrectBillDetailsException;
import exceptions.InvalidCustomerAccountException;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author shepard
 */
@Stateful
@TransactionManagement(value=TransactionManagementType.BEAN)
public class bankTransaction implements bankTransactionRemote {
    @PersistenceContext(unitName = "BankDS-ejbPU")
    private EntityManager em;
    
    @Resource
    private UserTransaction userTransaction;
    
    UserEntity recipient;
    UserEntity sender;
    Long amount;
    
    @Override
    public boolean setBeneficiary(String username){
        try {
            recipient = (UserEntity) em.createNamedQuery("UserEntity.findByUsername").setParameter("username", username).getSingleResult();
            return true;
        } catch (NoResultException nre) {
            System.out.println("No user found!");
            return false;
        }
    }
    
//    public void setRecipient(UserEntity e){
//        rid = e.getId();
//        rfirstname = e.getFirstname();
//        rsecondname = e.getSecondname();
//        rusername = e.getUsername();
//        rpassword = e.getPassword();
//        rcheckingaccount = e.getCheckingaccount(); 
//        rcreditcard = e.getCreditcard();
//    }
    
//    public void setSender(UserEntity e){
//        sid = e.getId();
//        sfirstname = e.getFirstname();
//        ssecondname = e.getSecondname();
//        susername = e.getUsername();
//        spassword = e.getPassword();
//        scheckingaccount = e.getCheckingaccount(); 
//        screditcard = e.getCreditcard();
//    }
    
    @Override
    public boolean setAmount(Long amount, String username){
        sender = (UserEntity) em.createNamedQuery("UserEntity.findByUsername").setParameter("username", username).getSingleResult();
        if (amount > sender.getCheckingaccount()){
            System.out.println("You don't have enough money");
            return false;
        } else{
            this.amount = amount;
            return true;
        }
    }
    
    @Override
    public List<String> getBeneficiary(){
        List<String> res = new LinkedList();
        res.add(recipient.getUsername());
        res.add(recipient.getFirstname());
        res.add(recipient.getSecondname());
        return res;
    }
    
    @Override
    public String getAmount(){
        return amount.toString();
    }
    
    @Override
    public String confirmTransaction(String username, String password) throws Exception{
        String finalAmount;
        try{
            userTransaction.begin();
            checkCredentials(username, password);
            checkRecipient();
            checkBillDetails();
            finalAmount = performBill();
            userTransaction.commit();
        } catch (InvalidCustomerAccountException ex){
            System.out.println(ex.getMessage());
            userTransaction.rollback();
            throw new InvalidCustomerAccountException("Error: Username or password not correct");
        } catch (IncorrectBillDetailsException ex){
            System.out.println(ex.getMessage());
            userTransaction.rollback();
            throw new InvalidCustomerAccountException("Error: Bill details not correct. Check the amount of the transaction");
        }
        System.out.println("Transaction is done!");
        return finalAmount;
    }
    
    
    public void checkCredentials(String user, String pwd) throws InvalidCustomerAccountException{
        if (!(sender.getUsername().equals(user) && sender.getPassword().equals(pwd))){
            throw new InvalidCustomerAccountException("Error: Username or password not correct");
        }
            
    }
    
    public void checkRecipient() throws InvalidCustomerAccountException{
        try{
            em.createNamedQuery("UserEntity.findByUsername").setParameter("username", recipient.getUsername()).getSingleResult();
        } catch (NoResultException nre) {
            System.out.println("No recipient found");
            throw new InvalidCustomerAccountException("Error: the recipient does not exist anymore.");
        }
    }
    
    public void checkBillDetails() throws IncorrectBillDetailsException{
        Long sendAmount = sender.getCheckingaccount();
        if ((sendAmount - amount) < 0)
            throw new IncorrectBillDetailsException("Error: Bill details not correct. Check the amount of the transaction");
    }
    
    public String performBill(){
        sender.setCheckingaccount(sender.getCheckingaccount() - amount);
        recipient.setCheckingaccount(recipient.getCheckingaccount() + amount);
        em.merge(sender);
        em.merge(recipient);
        return sender.getCheckingaccount().toString();
    }
}
