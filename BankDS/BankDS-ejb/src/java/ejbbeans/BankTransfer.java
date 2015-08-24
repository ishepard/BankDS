/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbbeans;

import ejbbeansremote.BankTransferRemote;
import entities.UserEntity;
import exceptions.IncorrectBillDetailsException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author shepard
 */
@Stateless
public class BankTransfer implements BankTransferRemote {
    @PersistenceContext(unitName = "BankDS-ejbPU")
    private EntityManager em;
    
    UserEntity user;
    
    @Override
    public String withdraw(Long amount, String startingaccount, String username) throws IncorrectBillDetailsException{
        try{
            user = (UserEntity) em.createNamedQuery("UserEntity.findByUsername").setParameter("username", username).getSingleResult();
            checkBillDetails(amount, startingaccount);

            if (startingaccount.equals("checkingaccount")){
                user.setCheckingaccount(user.getCheckingaccount() - amount);
                em.merge(user);
                return user.getCheckingaccount().toString();
            } else{
                user.setCreditcard(user.getCreditcard() - amount);
                em.merge(user);
                return user.getCreditcard().toString();
            }

        } catch (IncorrectBillDetailsException ex){
            System.out.println(ex.getMessage());
            throw new IncorrectBillDetailsException("You don't have enough money");
        } catch (NoResultException nre) {
            System.out.println("No user found!");
            throw new IncorrectBillDetailsException("User not found");
        }
    }
    
    public void checkBillDetails(Long amount, String from) throws IncorrectBillDetailsException{
        Long sendAmount;
        if (from.equals("checkingaccount")){
            sendAmount = user.getCheckingaccount();
        } else
            sendAmount = user.getCreditcard();
        
        if ((sendAmount - amount) < 0)
            throw new IncorrectBillDetailsException("You don't have enough money");
    }
    
    @Override
    public String refill(Long amount, String from, String username){
        user = (UserEntity) em.createNamedQuery("UserEntity.findByUsername").setParameter("username", username).getSingleResult();
        if (from.equals("checkingaccount")){
            user.setCheckingaccount(user.getCheckingaccount() + amount);
            em.merge(user);
            return user.getCheckingaccount().toString();
        } else{
            user.setCreditcard(user.getCreditcard() + amount);
            em.merge(user);
            return user.getCreditcard().toString();
        }

        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
