package org.iotope.context;

import java.util.Map;


public interface Filter {

    public boolean match(ExecutionContext context);

    public void configure(Map<String, String> properties);
}
