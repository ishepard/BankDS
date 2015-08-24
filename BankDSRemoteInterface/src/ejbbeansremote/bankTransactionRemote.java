/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbbeansremote;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author shepard
 */
@Remote
public interface bankTransactionRemote {
    public boolean setBeneficiary(String username);
    public boolean setAmount(Long amount, String username);
    public List<String> getBeneficiary();
    public String getAmount();
    public String confirmTransaction(String username, String password) throws Exception;
}
