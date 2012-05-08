package org.iotope.context;


public interface Filter {

    public boolean match(ExecutionContext context);
}
