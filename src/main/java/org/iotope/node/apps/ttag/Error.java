package org.iotope.node.apps.ttag;

import nu.xom.Document;

import org.apache.http.HttpResponse;

public class Error extends ACSBase<Error> {
    
    public Error(Document doc) throws Exception {
        super(doc, "/error:error",Error.class);
    }
    
    public Error(HttpResponse response) throws Exception {
        super(response, "/error:error",Error.class);
    }

    public Error(String template) throws Exception {
        super(template, "/error:error",Error.class);
    }

    public String getMessage() {
        return getAttributeValue("message");
    }
    
    public String getErrorCode() {
        return getAttributeValue("errorCode");
    }
    
}
