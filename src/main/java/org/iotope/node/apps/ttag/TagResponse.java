package org.iotope.node.apps.ttag;


public interface TagResponse {
    
    public abstract String getContainerAttribute(String name);
    
    public abstract boolean hasTransactionFeedback();
    
    public abstract String getTransactionId();
    
    public abstract boolean existNodeTransactionId();
    
    public abstract boolean isDuplicate();
    
    public abstract String getTransactionErrorCode();
    
    public abstract String getSystemErrorCode();
    
    public abstract String getSystemMessage();

    public abstract String getContainerName();
    
    public abstract boolean isSoapFault();
    
    public abstract String getSoapFault();

}