package org.iotope.gateway.http;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;


public interface HttpGateway {
    
    public HttpResponse execute(HttpRequest request, HttpCredentials credentials);
}
