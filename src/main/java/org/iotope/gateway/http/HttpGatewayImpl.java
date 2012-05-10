package org.iotope.gateway.http;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.inject.Singleton;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.VersionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HttpGatewayImpl implements HttpGateway {
    
    ThreadSafeClientConnManager connectionManager;
    ProxySelectorRoutePlanner routePlanner = null;
    
    HttpGatewayImpl() {
        
        //        SchemeRegistry schemeRegistry;
        //        try {
        //            schemeRegistry = createSchemeRegistry();
        //        } catch (Exception e) {
        //            throw new RuntimeException(e);
        //        }
        //        
        //        connectionManager = new ThreadSafeClientConnManager(schemeRegistry, 30, TimeUnit.SECONDS);
        //        // Increase max total connection to 200
        //        connectionManager.setMaxTotal(200);
        //        // Increase default max connection per route to 20
        //        connectionManager.setDefaultMaxPerRoute(50);
        //        
        //        
        //        String proxyType = _context.get("http.proxy");
        //        if (proxyType == null) {
        //            throw new RuntimeException("Property http.proxy not set.");
        //        }
        //        if ("http".equals(proxyType)) {
        //            final String proxyHost = _context.get("http.proxy.host");
        //            final int proxyPort = Integer.valueOf(_context.get("http.proxy.port"));
        //            ProxySelector proxySelector = new ProxySelector() {
        //                
        //                @Override
        //                public List<Proxy> select(URI uri) {
        //                    List<Proxy> list = new ArrayList<Proxy>();
        //                    list.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
        //                    return list;
        //                }
        //                
        //                @Override
        //                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        //                    logger.error("URI is {} with exception {}", uri.toString(), ioe.getMessage());
        //                }
        //            };
        //            routePlanner = new ProxySelectorRoutePlanner(schemeRegistry, proxySelector);
        //            logger.info("Setting proxy to http://{}:{}.", proxyHost, proxyPort);
        //        } else if ("internal".equals(proxyType)) {
        //            routePlanner = new ProxySelectorRoutePlanner(schemeRegistry, ProxySelector.getDefault());
        //            logger.info("Using internal proxy, make sure the proxy vm parameters are set.");
        //        } else if ("none".equals(proxyType)) {
        //            // No proxy type is selected
        //            logger.info("Using no proxy.");
        //        }
        //        
    }
    
    final static Logger logger = LoggerFactory.getLogger(HttpGatewayImpl.class);
    
    HttpClient httpClient;
    
    private HttpParams createHttpParams() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        
        // determine the release version from packaged version info
        final VersionInfo vi = VersionInfo.loadVersionInfo("org.apache.http.client", getClass().getClassLoader());
        final String release = (vi != null) ? vi.getRelease() : VersionInfo.UNAVAILABLE;
        HttpProtocolParams.setUserAgent(params, "IOTOPE-Node/0.0 (Apache HTTP Client " + release + ")");
        return params;
    }
    
    private SchemeRegistry createOptionalSSLSchemeRegistry() throws KeyManagementException, NoSuchAlgorithmException {
        SchemeRegistry schemeRegistry = null;
        String sslManager = "full_trust";
        if (sslManager == null) {
            throw new RuntimeException("Property http.ssl.trustmanager not set.");
        }
        if ("full_trust".equals(sslManager)) {
            Scheme http = new Scheme("http", PlainSocketFactory.getSocketFactory(), 80);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(new KeyManager[] { new X509TestKeyManager() }, new TrustManager[] { new X509FullTrustManager() }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            
            SSLSocketFactory sf = new SSLSocketFactory(sslContext);
            sf.setHostnameVerifier(new X509TestHostnameVerifier());
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            
            Scheme https = new Scheme("https", sf, 443);
            
            schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(http);
            schemeRegistry.register(https);
        } else if ("internal".equals(sslManager)) {
            logger.info("Using internal ");
        } else {
            throw new RuntimeException("Unknown ssl.trustmanager property.");
        }
        return schemeRegistry;
    }
    
    private SchemeRegistry createSchemeRegistry() throws KeyManagementException, NoSuchAlgorithmException {
        SchemeRegistry schemeRegistry = createOptionalSSLSchemeRegistry();
        
        // If no schema registry is set, let's create a default one.
        if (schemeRegistry == null) {
            schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        }
        return schemeRegistry;
    }
    
    private HttpClient createHttpClient() {
        HttpParams httpParams = createHttpParams();
        DefaultHttpClient httpclient = new DefaultHttpClient(connectionManager, httpParams);
        
        if (routePlanner != null) {
            httpclient.setRoutePlanner(routePlanner);
        }
        
        return httpclient;
    }
    
    public synchronized HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = createHttpClient();
        }
        return httpClient;
    }
    
    @Override
    public HttpResponse execute(HttpRequest request, HttpCredentials credentials) {
        HttpClient client = getHttpClient();
        try {
            URI uri = ((HttpUriRequest) request).getURI();
            
            if (credentials != null) {
                UsernamePasswordCredentials auth = new UsernamePasswordCredentials(credentials.getUser(), credentials.getPassword());
                ((AbstractHttpClient) client).getCredentialsProvider().setCredentials(new AuthScope(uri.getHost(), uri.getPort()), auth);
            }
            
            HttpResponse response = client.execute((HttpUriRequest) request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                response.setEntity(new BufferedHttpEntity(entity));
            }
            return response;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
