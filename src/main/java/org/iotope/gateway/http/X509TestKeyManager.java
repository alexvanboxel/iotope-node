package org.iotope.gateway.http;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509KeyManager;

public class X509TestKeyManager implements X509KeyManager {

    public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
        return null;
    }

    public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
        return null;
    }

    public X509Certificate[] getCertificateChain(String arg0) {
        return null;
    }

    public String[] getClientAliases(String arg0, Principal[] arg1) {
        return null;
    }

    public PrivateKey getPrivateKey(String arg0) {
        return null;
    }

    public String[] getServerAliases(String arg0, Principal[] arg1) {
        return null;
    }
    
}
