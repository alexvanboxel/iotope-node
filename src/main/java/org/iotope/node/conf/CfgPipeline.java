package org.iotope.node.conf;

import java.util.ArrayList;
import java.util.List;

public class CfgPipeline {
    
    List<CfgApplication> applications = new ArrayList<CfgApplication>();
    
    public List<CfgApplication> getApplications() {
        return applications;
    }
    
    public void addApplications(CfgApplication applications) {
        this.applications.add(applications);
    }
    
}
