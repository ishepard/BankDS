/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author shepard
 */
@Entity
@Table(name = "USERENTITY")
@NamedQueries({
        @NamedQuery(name = "UserEntity.findById", query = "SELECT e FROM UserEntity e WHERE e.id = :id"),
        @NamedQuery(name = "UserEntity.findByFirstname", query = "SELECT e FROM UserEntity e WHERE e.firstname = :firstname"),
        @NamedQuery(name = "UserEntity.findBySecondname", query = "SELECT e FROM UserEntity e WHERE e.secondname = :secondname"),
        @NamedQuery(name = "UserEntity.findByUsername", query = "SELECT e FROM UserEntity e WHERE e.username = :username"), 
        @NamedQuery(name = "UserEntity.findByPassword", query = "SELECT e FROM UserEntity e WHERE e.password = :password"),
        @NamedQuery(name = "UserEntity.findByFirstAndSecondname", query = "SELECT e FROM UserEntity e WHERE e.firstname = :firstname AND e.secondname = :secondname")
    })
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "FIRSTNAME", nullable = false)
    protected String firstname;
    
    @Column(name = "SECONDNAME", nullable = false)
    protected String secondname;
    
    @Column(name = "USERNAME", nullable = false, unique = true)
    protected String username;
    
    @Column(name = "PASSWORD", nullable = false)
    protected String password;
    
    @Column(name = "CHECKINGACCOUNT", nullable = false)
    protected Long checkingaccount;
    
    @Column(name = "CREDITCARD", nullable = false)
    protected Long creditcard;

    public UserEntity() {
    }

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

    public UserEntity(Long id, String firstname, String secondname, String username, String password, Long checkingaccount, Long creditcard) {
        this.id = id;
        this.firstname = firstname;
        this.secondname = secondname;
        this.username = username;
        this.password = password;
        this.checkingaccount = checkingaccount;
        this.creditcard = creditcard;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserEntity)) {
            return false;
        }
        UserEntity other = (UserEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UserEntity[ id=" + id + " ]";
    }
    
}
