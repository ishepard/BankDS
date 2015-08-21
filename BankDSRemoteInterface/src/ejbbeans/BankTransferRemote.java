/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbbeans;

import javax.ejb.Remote;

/**
 *
 * @author shepard
 */
@Remote
public interface BankTransferRemote {
    public String refill(Long amount, String from, String username);
    public String withdraw(Long amount, String startingaccount, String username) throws Exception;
}
