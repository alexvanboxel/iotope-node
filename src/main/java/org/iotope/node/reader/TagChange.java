package org.iotope.node.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iotope.nfc.tag.NfcTarget;
import org.iotope.nfc.tag.TagContent;
import org.iotope.pipeline.model.App;
import org.iotope.pipeline.model.Field;
import org.iotope.pipeline.model.Reader;
import org.iotope.util.IOUtil;

public class TagChange /*implements JSON.Convertible*/ {
    
    public enum Event {
        ADDED, REMOVED
    };
    
    public TagChange(Event event, Reader reader, int slot, NfcTarget target) {
        super();
        this.event = event;
        this.reader = reader;
        this.slot = slot;
        this.target = target;
        this.fields = new ArrayList<Field>();
    }
    
    public Event getEvent() {
        return event;
    }
    
    public String getNfcId() {
        if (target != null) {
            return IOUtil.hexbin(target.getNfcId());
        }
        return null;
    }
    
    public void setApplication(App app) {
        this.application = app;
    }
    
    public void addField(Field field) {
        fields.add(field);
    }

    public void addTagContent(TagContent tagContent) {
        this.tagContent = tagContent;
    }
    
    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public App getApplication() {
        return application;
    }

    public Reader getReader() {
        return reader;
    }

    public int getSlot() {
        return slot;
    }
    
    public TagContent getContent() {
        return tagContent;
    }
    
    private Event event;
    private Reader reader;
    private int slot;
    private NfcTarget target;
    private List<Field> fields;
    private App application;
    private TagContent tagContent;
}
