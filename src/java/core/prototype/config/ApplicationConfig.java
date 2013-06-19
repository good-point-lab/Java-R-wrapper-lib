package core.prototype.config;

import core.prototype.dao.DaoConfig;
import core.prototype.dao.DaoProfilesConfig;
import core.prototype.domain.DomainConfig;
import core.prototype.hadoop.HadoopConfig;
import core.prototype.rengine.REngineConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
// @ComponentScan(basePackages = {"core.prototype.installer"} ),excludeFilters={@Filter(Configuration.class)}
@Import({
    DomainConfig.class,
    PropertiesFileReader.class,
    DaoProfilesConfig.class,
    DaoConfig.class,
    REngineConfig.class,
    HadoopConfig.class
})
public class ApplicationConfig {

    @Autowired
    private ApplicationContext context;
}