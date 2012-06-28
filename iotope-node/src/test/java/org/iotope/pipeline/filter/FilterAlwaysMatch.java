package org.iotope.pipeline.filter;


import java.util.Map;

import org.iotope.IotopeFilter;
import org.iotope.context.ExecutionContext;
import org.iotope.context.Filter;

@IotopeFilter(domain = "test.iotope.org", name = "true")
public class FilterAlwaysMatch implements Filter {
    
    @Override
    public boolean match(ExecutionContext context) {
        return true;
    }
    
    @Override
    public void configure(Map<String, String> properties) {
    }
    
}
