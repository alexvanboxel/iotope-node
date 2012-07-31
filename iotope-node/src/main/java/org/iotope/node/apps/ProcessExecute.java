package org.iotope.node.apps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.iotope.IotopeAction;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.MetaData;
import org.iotope.pipeline.model.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IotopeAction(domain = "iotope.org", name = "process")
public class ProcessExecute implements Application {
    private static Logger Log = LoggerFactory.getLogger(ProcessExecute.class);
    
    @Override
    public void execute(ExecutionContext context) {
        String workdir = (String) context.getField("workdir");
        
        List<String> cmdlist = new ArrayList<String>();
        String process = (String) context.getField("process");
        if(process == null) {
            // nothing to do
            return;
        }
        cmdlist.add(process);
        for(int i=0;i<10;i++) {
            String arg = (String) context.getField("arg"+i);
            if(arg!=null && arg.length() > 0) {
                cmdlist.add(arg);
            }
            else {
                break;
            }
        }
        
        File work = null;
        if(workdir != null && workdir.length() != 0) {
            work = new File(workdir);
            if(!work.exists()) {
                work = null;
            }
        }
        if(work == null) {
            work = new File(".");
        }

        try {
            Runtime.getRuntime().exec(cmdlist.toArray(new String[0]), null, work);
        } catch (IOException e) {
            Log.error(e.getMessage());
        }
    }
    
    private static class ProcessExecuteMetaData implements MetaData {
        
        @Override
        public Collection<Field> getFields() {
            List<Field> fields = new ArrayList<Field>();
            fields.add(new Field("workdir", "xs:string", "WorkDir", "Working directory of the process."));
            fields.add(new Field("process", "xs:string", "Process", "Message to display."));
            fields.add(new Field("arg0", "xs:string", "Arg0", "First Argument."));
            fields.add(new Field("arg1", "xs:string", "Arg1", "Second Argument."));
            fields.add(new Field("arg2", "xs:string", "Arg2", "Third Argument."));
            fields.add(new Field("arg3", "xs:string", "Arg3", "3th Argument."));
            fields.add(new Field("arg4", "xs:string", "Arg4", "4th Argument."));
            fields.add(new Field("arg5", "xs:string", "Arg5", "5th Argument."));
            fields.add(new Field("arg6", "xs:string", "Arg6", "6th Argument."));
            fields.add(new Field("arg7", "xs:string", "Arg7", "7th Argument."));
            fields.add(new Field("arg8", "xs:string", "Arg8", "8th Argument."));
            fields.add(new Field("arg9", "xs:string", "Arg9", "9th Argument."));
            return fields;
        }
        
        @Override
        public String getDisplayName() {
            return "Process Execute";
        }
        
        @Override
        public String getDescription() {
            return "Execute a local process.";
        }
        
    }
    
    @Override
    public MetaData getMetaData() {
        return new ProcessExecuteMetaData();
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        // Nothing to configure
    }
}
