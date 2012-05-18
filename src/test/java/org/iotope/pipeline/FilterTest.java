package org.iotope.pipeline;

import junit.framework.Assert;

import org.iotope.node.UnitNode;
import org.iotope.node.conf.Cfg;
import org.iotope.node.conf.Configuration;
import org.junit.Test;

public class FilterTest {
    
    @Test
    public void x() throws Exception {
        ExecutionPipeline pipeline = UnitNode.instance(ExecutionPipeline.class);
        ExecutionContextImpl executionContext = new ExecutionContextImpl();
        
        Configuration c = new Configuration();
        c.load("/org/iotope/pipeline/filter.xml");
        Cfg cfg = c.getConfig();
        pipeline.initPipeline(cfg, executionContext);
        pipeline.startPipeline();
        Object result = executionContext.getField("test.iotope.org", "test", "result");
        Assert.assertNotNull(result);
    }
    
}
