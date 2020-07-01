/*
 * https://qiita.com/xx2xyyy/items/ef083a29d4405b9845b2
 */

package vavi.net.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


@ConditionalOnWebApplication
@Configuration
class WebSocketConfig {

    @Bean
    public NotificationEndpoint notificationEndpoint(@Autowired WebHookService webHookService) {
      return new NotificationEndpoint(webHookService);
    }

    @Bean
    public ServerEndpointExporter endpointExporter() {
      return new ServerEndpointExporter();
    }

    @Bean
    public CustomSpringConfigurator customSpringConfigurator() {
        return new CustomSpringConfigurator(); // This is just to get context
    }
}
