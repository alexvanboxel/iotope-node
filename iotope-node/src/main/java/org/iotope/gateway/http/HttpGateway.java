package org.iotope.gateway.http;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;


public interface HttpGateway {
    
    public HttpResponse execute(HttpRequest request, HttpCredentials credentials);
}
