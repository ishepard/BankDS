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
public class IncorrectBillDetailsException extends Exception {

    /**
     * Creates a new instance of <code>IncorrectBillDetailsException</code>
     * without detail message.
     */
    public IncorrectBillDetailsException() {
    }

    /**
     * Constructs an instance of <code>IncorrectBillDetailsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public IncorrectBillDetailsException(String msg) {
        super(msg);
    }
}
