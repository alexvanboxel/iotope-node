package org.iotope.node.apps;

import org.iotope.IotopeApplication;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.MetaData;

@IotopeApplication(domain="iotope.org",name="ndef")
public class NdefActions implements Application {

    @Override
    public void execute(ExecutionContext context) {
    }

    @Override
    public MetaData getMetaData() {
        return null;
    }
}
