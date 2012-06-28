package org.iotope.node.apps.ttag;

public enum TagType {
    ULTRALIGHT("urn:rfid:iso14443:a:mifare:ultralight:", 0),
    DESFIRE("urn:rfid:iso14443:a:mifare:desfire:", 3),
    VIRTUAL("urn:virtual:uuid:", 256);
    
    private String urn;
    private int typeId;
    
    private TagType(String urn, int typeId) {
        this.urn = urn;
        this.typeId = typeId;
    }
    
    public String getURN() {
        return urn;
    }

    public int getTypeId() {
        return typeId;
    }
    
    public static TagType fromId(int id) {
        for (TagType type : TagType.values()) {
            if (type.getTypeId() == id) {
                return type;
            }
        }
        throw new RuntimeException("Unknown tag type id.");
    }
}
