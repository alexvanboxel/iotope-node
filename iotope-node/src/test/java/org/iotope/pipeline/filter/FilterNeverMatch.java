package org.iotope.pipeline.filter;


import java.util.Map;

import org.iotope.IotopeFilter;
import org.iotope.context.ExecutionContext;
import org.iotope.context.Filter;

@IotopeFilter(domain = "test.iotope.org", name = "false")
public class FilterNeverMatch implements Filter {
    
    @Override
    public boolean match(ExecutionContext context) {
        return false;
    }
    
    @Override
    public void configure(Map<String, String> properties) {
    }
    
}
