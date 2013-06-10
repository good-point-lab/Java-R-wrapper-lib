package core.prototype.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DomainConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = "domain")
    @Scope("prototype")
    public DomainApi domain() {
        DomainApi rApi = new DomainBase();
        return rApi;
    }
}
