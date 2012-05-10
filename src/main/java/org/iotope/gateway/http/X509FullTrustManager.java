package org.iotope.gateway.http;

import java.security.cert.CertificateException;

import javax.net.ssl.X509TrustManager;

public class X509FullTrustManager implements X509TrustManager {

    public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
         return null;
    }
}
