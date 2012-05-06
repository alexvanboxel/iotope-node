package org.iotope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IotopeApplication {

    String domain();

    String name();
    
}
