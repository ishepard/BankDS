/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbbeans;

import entities.UserEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author shepard
 */
@Stateless
public class AdminBean implements AdminBeanRemote {
    @PersistenceContext(unitName = "BankDS-ejbPU")
    private EntityManager em;

    @Override
    public String printMessage() {
        return "Hello World!";
    }
    
    @Override
    public void createUser(Long id, String firstname, String secondname, String username, String password, Long checkingaccount, Long creditcard){
        System.out.println("Admin requested to create a user...");
        UserEntity e = new UserEntity(id, firstname, secondname, username, password, checkingaccount, creditcard);
        em.persist(e);
    }
    
    @Override
    public List<Map<String, String>> getAll() {
        List<UserEntity> l = em.createQuery("SELECT e FROM UserEntity e").getResultList();

        List res = new ArrayList();
        for (UserEntity i : l) {
            HashMap<String, String> vehicleView = new HashMap<>();
            vehicleView.put("id", Long.toString(i.getId()));
            vehicleView.put("firstname", i.getFirstname());
            vehicleView.put("secondname", i.getSecondname());
            vehicleView.put("username", i.getUsername());
            vehicleView.put("password", i.getPassword());
            vehicleView.put("checkingaccount", Long.toString(i.getCheckingaccount()));
            vehicleView.put("creditcard", Long.toString(i.getCreditcard()));
            vehicleView.put("type", i.getClass().getSimpleName());
            res.add(vehicleView);
        }
        return res;
    }
    
    @Override
    public void deleteUser(Long id) {
        System.out.println("Admin requested to delete the user with id " + id);
        try {
            UserEntity ue = (UserEntity) em.createNamedQuery("UserEntity.findById").setParameter("id", id).getSingleResult();
            em.remove(ue);
        } catch (NoResultException nre) {
            System.out.println("No user found with is " + id.toString());
        }
    }

    @Override
    public String findUserByUsername(String username){
        try {
            UserEntity res = (UserEntity) em.createNamedQuery("UserEntity.findByUsername").setParameter("username", username).getSingleResult();
            return res.getPassword();
        } catch (NoResultException nre) {
            System.out.println("No user found!");
            return null;
        }
    }
    
    @Override
    public List<String> getUserEntity(String username){
        UserEntity user = (UserEntity) em.createNamedQuery("UserEntity.findByUsername").setParameter("username", username).getSingleResult();
        List res = new LinkedList();
        res.add(user.getId());
        res.add(user.getFirstname());
        res.add(user.getSecondname());
        res.add(user.getUsername());
        res.add(user.getPassword());
        res.add(user.getCheckingaccount());
        res.add(user.getCreditcard());
        return res;
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
