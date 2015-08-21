/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsfbeans;

import connector.RemoteConnector;
import ejbbeans.AdminBeanRemote;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

/**
 *
 * @author shepard
 */
@ManagedBean
@RequestScoped
public class jsfAdminBean {

    RemoteConnector rc;
    AdminBeanRemote a;
    
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
    
    /**
     * Creates a new instance of jsfAdminBean
     */
    public jsfAdminBean() {
        rc = new RemoteConnector("BankDS", "BankDS-ejb");
        System.out.println("Obtained Remote accessor: " + rc);
        try {
            a = rc.lookup("AdminBean", AdminBeanRemote.class.getName());
        } catch (NamingException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public String create(){
        System.out.println("Creating new user...");
        try{
            a.createUser(id, firstname, secondname, username, password, checkingaccount, creditcard);
            System.out.println("New user created!");
            return "";
        } catch (javax.ejb.EJBTransactionRolledbackException err){
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                           "Fail in creating a new user!",
                                           "Constraints violation!");
            context.addMessage(null, message);
            return null;
        }
    }
    
    public List<Map<String, String>> getAll(){
        List<Map<String, String>> l = a.getAll();
        return l;
    }
    
    public String delete(String id){
        System.out.println("Deleting user with id " + id);
        a.deleteUser(Long.parseLong(id));
        return "";
    }
    
    public String showMessage(){
        return a.printMessage();
    }
    
}
