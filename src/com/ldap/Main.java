package com.ldap;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Properties;


/*
Created by Sudeep Srivastava on 1-2-2018
 */

public class Main {


    public static void main(String[] args) {
        ADResult adResult = authenticateJndi("USERNAME", "PASSWORD");
    }

    //Call this method
    public static ADResult authenticateJndi(String username, String password) {
        ADResult adResult = null;
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, Config.environment);
        props.put(Context.PROVIDER_URL, Config.ldapUrl);
        props.put(Context.SECURITY_PRINCIPAL, Config.adminUserName);
        props.put(Context.SECURITY_CREDENTIALS, Config.adminPassword);
        InitialDirContext context = null;
        try {
            context = new InitialDirContext(props);
        } catch (Exception e1) {
            throw new RuntimeException("Wrong Admin Credentials");
        }
        SearchControls ctrls = new SearchControls();
        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> answers = null;
        try {
            answers = context.search("DC=infoedge,DC=com", "(mail=" + username + ")", ctrls);
        } catch (NamingException e) {
        }
        try {
            SearchResult result = answers.nextElement();
            String user = result.getNameInNamespace();
            String userId = result.getAttributes().get("info").toString();
            String name = result.getAttributes().get("name").toString();

            if ((userId.isEmpty()) || name.isEmpty()) {
                throw new RuntimeException("User not found @ Org");
            }
            name = name.split(": ")[1];
            userId = userId.split(": ")[1];
            adResult = new ADResult(name, userId);
            try {
                props = new Properties();
                props.put(Context.INITIAL_CONTEXT_FACTORY, Config.environment);
                props.put(Context.PROVIDER_URL, Config.ldapUrl);
                props.put(Context.SECURITY_PRINCIPAL, user);
                props.put(Context.SECURITY_CREDENTIALS, password);
                context = new InitialDirContext(props);
            } catch (Exception e) {
                throw new RuntimeException("Wrong User Name or Password");
            }
            return adResult;
        } catch (NullPointerException e) {
            throw new RuntimeException("User not found in organization");
        }
    }

}
