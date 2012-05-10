package org.iotope.gateway.http;


public class HttpCredentials {

    public HttpCredentials(String user, String password) {
        super();
        this.user = user;
        this.password = password;
    }
    private String user;
    private String password;
    public String getUser() {
        return user;
    }
    public String getPassword() {
        return password;
    }
    
    

}
