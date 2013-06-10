package core.prototype.config;

import org.springframework.beans.factory.annotation.Autowired;

/*
 * Example bean that is not configured with Spring container, but put in the context 
 * by the Autowire Capable Bean Factory.  It allows injecting the rest of the configured 
 * dependensies.
 */
public class BeanWithAutowiredDependencyExample {

    @Autowired
    private PropertiesFileReader propertiesConfig;
    
    private Object argument;

    public BeanWithAutowiredDependencyExample(Object argument) {
        this.argument = argument;
    }

    public BeanWithAutowiredDependencyExample() {
    }

    public String printAutowiredProperty() {
        return  propertiesConfig.getDbType();
    }
}
