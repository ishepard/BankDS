/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author shepard
 */
public class InvalidCustomerAccountException extends Exception {

    /**
     * Creates a new instance of <code>InvalidCustomerAccountException</code>
     * without detail message.
     */
    public InvalidCustomerAccountException() {
    }

    /**
     * Constructs an instance of <code>InvalidCustomerAccountException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidCustomerAccountException(String msg) {
        super(msg);
    }
}
