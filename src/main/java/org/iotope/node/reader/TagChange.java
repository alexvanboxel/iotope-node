package org.iotope.node.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.ajax.JSON.Output;
import org.iotope.nfc.ndef.NdefMessage;
import org.iotope.nfc.ndef.NdefRecord;
import org.iotope.nfc.tag.NfcTarget;
import org.iotope.nfc.tag.TagContent;
import org.iotope.node.model.Application;
import org.iotope.node.model.FieldDefinition;
import org.iotope.node.model.FieldValue;
import org.iotope.util.IOUtil;

public class TagChange implements JSON.Convertible {
    
    public enum Event {
        ADDED, REMOVED
    };
    
    public TagChange(Event event, Reader reader, int slot, NfcTarget target) {
        super();
        this.event = event;
        this.reader = reader;
        this.slot = slot;
        this.target = target;
        this.fields = new ArrayList<FieldValue>();
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
    
    public void setApplication(Application application) {
        this.application = application;
    }
    
    public void addField(FieldValue value) {
        fields.add(value);
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public void fromJSON(Map map) {
    }
    
    @Override
    public void toJSON(Output out) {
        out.add("type", "TagChange");
        out.add("event", event);
        out.add("reader", reader);
        out.add("slot", slot);
        if (target != null) {
            out.add("nfcid", getNfcId());
        }
        Map<String, Object>[] values = new HashMap[fields.size()];
        int ix = 0;
        for (FieldValue value : fields) {
            FieldDefinition definition = value.getField();
            values[ix] = new HashMap<String, Object>();
            values[ix].put("name", definition.getName());
            values[ix].put("displayName", definition.getDisplayName());
            values[ix].put("type", definition.getType());
            values[ix].put("description", definition.getDescription());
            values[ix].put("value", value.getValue());
            ix++;
        }
        out.add("fields", values);
        if (application != null) {
            Map<String, Object> app = new HashMap<String, Object>();
            app.put("appId", application.getAppId());
            out.add("application", app);
        }
        if (tagContent != null) {
            Map<String, Object>[] cnt = new HashMap[tagContent.size()];
            for (ix = 0; ix < tagContent.size(); ix++) {
                TagContent.Content tc = tagContent.get(ix);
                cnt[ix] = new HashMap<String, Object>();
                cnt[ix].put("type", tc.getType());
                switch (tc.getType()) {
                case NDEF:
                    TagContent.NdefContent ndefContent = (TagContent.NdefContent)tc;
                    NdefMessage message = ndefContent.getNdef();
                    Map<String, Object>[] msg = new HashMap[message.size()];
                    for(int nx = 0;nx<message.size();nx++) {
                        NdefRecord record = message.getRecord(nx);
                        msg[nx] = new HashMap<String, Object>();
                        msg[nx].put("type", "ndef");
                        msg[nx].put("content", record.getContent());
                    }
                    cnt[ix].put("content", msg);
                    break;
                case LEGACY_HASH:
                case MEMORY_RW_BLOCK:
                default:
                    TagContent.ByteContent byteContent = (TagContent.ByteContent)tc;
                    cnt[ix].put("content", IOUtil.hexbin(byteContent.getContent()));
                }
            }
            out.add("content", cnt);
        }
    }
    
    public void addTagContent(TagContent tagContent) {
        this.tagContent = tagContent;
    }
    
    public List<FieldValue> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public Application getApplication() {
        return application;
    }

    private Event event;
    private Reader reader;
    private int slot;
    private NfcTarget target;
    private List<FieldValue> fields;
    private Application application;
    private TagContent tagContent;
}
