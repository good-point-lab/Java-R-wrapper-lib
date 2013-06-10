package core.prototype.rengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class REngineConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = "rEngine")
    @Scope("prototype")
    public REngineApi rEngine(RServeParameters params) {
        Rserve server = (Rserve) this.applicationContext.getBean("rServe", new Object[]{params});
        if (params.isIsLocal() && !server.isRServerRunning()) {
            server.startUpRServer();
        }
        REngineApi rApi = new REngineBase(server,
                params.getLocalRScriptsDirPath(),
                params.getLocatTempDirPath());
        return rApi;
    }

    @Bean(name = "rServe")
    @Scope("prototype")
    Rserve rEngineServer(RServeParameters params) {
        Rserve server = new Rserve(params);
        return server;
    }
}
