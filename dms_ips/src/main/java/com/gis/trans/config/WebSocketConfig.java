package com.gis.trans.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

   // @Bean
    public ServerEndpointExporter createServerEndpointExporer(){
        return new ServerEndpointExporter();
    }

}
