package org.iotope.pipeline;

import junit.framework.Assert;

import org.iotope.node.UnitNode;
import org.iotope.node.conf.Cfg;
import org.iotope.node.conf.Configuration;
import org.junit.Test;

public class FilterTest {
    
    private void test(String file, boolean executed) throws Exception {
        ExecutionPipeline pipeline = UnitNode.instance(ExecutionPipeline.class);
        ExecutionContextImpl executionContext = new ExecutionContextImpl();
        
        Configuration c = new Configuration();
        c.loadResource("/org/iotope/pipeline/filter_" + file + ".xml");
        Cfg cfg = c.getConfig();
        pipeline.initPipeline(cfg, executionContext);
        pipeline.startPipeline();
        Object result = executionContext.getField("test.iotope.org", "test", "result");
        if (executed) {
            Assert.assertNotNull(result);
            Assert.assertEquals("executed", result);
        } else {
            Assert.assertNull(result);
        }
    }
    
    @Test
    public void nofilter() throws Exception {
        test("nofilter", true);
    }
    
    @Test
    public void I_0() throws Exception {
        test("I_0", false);
    }
    
    @Test
    public void I_1() throws Exception {
        test("I_1", true);
    }
    
    
    @Test
    public void E_0() throws Exception {
        test("E_0", true);
    }
    
    
    @Test
    public void E_1() throws Exception {
        test("E_1", false);
    }
    
    @Test
    public void I_0_I_0() throws Exception {
        test("I_0_I_0", false);
    }
    
    @Test
    public void I_0_I_1() throws Exception {
        test("I_0_I_1", true);
    }
    
    @Test
    public void I_1_I_0() throws Exception {
        test("I_1_I_0", true);
    }
    
    @Test
    public void I_1_I_1() throws Exception {
        test("I_1_I_1", true);
    }
    
    
    @Test
    public void E_0_E_0() throws Exception {
        test("E_0_E_0", true);
    }
    
    @Test
    public void E_0_E_1() throws Exception {
        test("E_0_E_1", false);
    }
    
    @Test
    public void E_1_E_0() throws Exception {
        test("E_1_E_0", false);
    }
    
    @Test
    public void E_1_E_1() throws Exception {
        test("E_1_E_1", false);
    }

    
    @Test
    public void E_0_I_0() throws Exception {
        test("E_0_I_0", false);
    }
    
    @Test
    public void E_0_I_1() throws Exception {
        test("E_0_I_1", true);
    }
    
    @Test
    public void E_1_I_0() throws Exception {
        test("E_1_I_0", false);
    }
    
    @Test
    public void E_1_I_1() throws Exception {
        test("E_1_I_1", false);
    }
}
