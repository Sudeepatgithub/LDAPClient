package com.ldap;

public interface Config {

    String organizationName = "Your Organization";
    String environment = "com.sun.jndi.ldap.LdapCtxFactory";
    String ldapUrl = "ldap://172.16.3.241:389"; // Replace with your organizations LDAP Server Address
    String adminPassword ="xxxxxxxx";
    String adminUserName="xXXXXXXxx";
}
