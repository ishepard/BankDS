/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author shepard
 */
public class RemoteConnector {
    public Context context;
    
    public Properties jndiProps = new Properties() {
        {
            put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
            // username
            // put(Context.SECURITY_PRINCIPAL, "user");
            // password
            //put(Context.SECURITY_CREDENTIALS, "pw");
            // This is an important property to set if you want to do EJB invocations via the remote-naming project
            put("jboss.naming.client.ejb.context", "true");
        }
    };

    public Properties appProperties;

    public RemoteConnector(String appName, String moduleName) {
        appProperties = new Properties();
        appProperties.put("appName", appName);
        appProperties.put("moduleName", moduleName);
    }

    public <T> T lookup(String bean, String beanInterface) throws NamingException {
        if (context == null) {
            context = new InitialContext(jndiProps);
        }

        return (T) context.lookup(appProperties.getProperty("appName") + "/"
                + appProperties.getProperty("moduleName") + "/"
                + bean + "!"
                + beanInterface);
    }
}
