/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbbeans;

import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author shepard
 */
@Remote
public interface AdminBeanRemote {
    public String printMessage();
    public void createUser(Long id, String firstname, String secondname, String username, String password, Long checkingaccount, Long creditcard);
    public void deleteUser(Long id);
    public List<Map<String, String>> getAll();
    public String findUserByUsername(String username);
    public List<String> getUserEntity(String username);
}
