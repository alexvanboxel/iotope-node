package org.iotope.pipeline;

import org.iotope.context.ExecutionContext;
import org.iotope.nfc.tag.NfcTarget;
import org.iotope.nfc.target.NfcTlv;
import org.iotope.node.model.Application;
import org.iotope.node.model.FieldValue;
import org.iotope.pipeline.model.App;
import org.iotope.pipeline.model.Field;

import java.util.*;

public class ExecutionContextImpl implements ExecutionContext {
    
    public void setFields(Application application, Collection<FieldValue> collection) {
        this.entityApplication = new App(application);
        switchContext(application.getDomain(), application.getName());
        this.fieldMeta = new ArrayList<Field>();
        for (FieldValue val : collection) {
            Field field = new Field(val);
            this.fieldMeta.add(field);
            setField(field.getName(), field.getValue());
        }
    }
    
    public void setTargetContent(NfcTlv targetContent) {
        this.targetContent = targetContent;
    }
    
    void switchContext(String domain, String application) {
        this.domain = domain;
        this.application = application;
    }
    
    public Collection<Field> getFields() {
        return fieldMeta;
    }
    
    public App getApplication() {
        return entityApplication;
    }
    
    @Override
    public Object getField(String variable) {
        return fields.get(urn(domain, application, variable));
    }
    
    @Override
    public Object getField(String domain, String application, String variable) {
        return fields.get(urn(domain, application, variable));
    }
    
    @Override
    public void setField(String variable, String value) {
        setField(domain, application, variable, value);
    }
    
    @Override
    public void setField(String domain, String application, String variable, String value) {
        fields.put(urn(domain, application, variable), value);
    }
    
    @Override
    public void executeNext(String domain, String application) {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void executeNext(String domain, String application, String variable, String value) {
        fields.put(urn(domain, application, variable), value);
        executeLast(domain, application);
    }
    
    @Override
    public void executeLast(String domain, String application) {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void executeLast(String domain, String application, String variable, String value) {
        fields.put(urn(domain, application, variable), value);
        executeLast(domain, application);
    }
    
    @Override
    public NfcTlv getTargetContent() {
        return targetContent;
    }
    
    
    @Override
    public NfcTarget getNfcTarget() {
        return target;
    }

    public void setNfcTarget(NfcTarget target) {
        this.target = target;
    }

    private String urn(String domain, String application) {
        return "urn:iotope.app:" + domain + ":" + application;
    }
    
    private String urn(String domain, String application, String variable) {
        return urn(domain, application) + ":" + variable;
    }
    
    private String domain;
    private String application;
    
    private App entityApplication;
    private List<Field> fieldMeta;
    private Map<String, Object> fields = new HashMap<String, Object>();
    private NfcTlv targetContent = new NfcTlv();
    private NfcTarget target;
}
