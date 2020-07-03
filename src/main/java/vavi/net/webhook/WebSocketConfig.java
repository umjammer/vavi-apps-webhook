/*
 * https://qiita.com/xx2xyyy/items/ef083a29d4405b9845b2
 */

package vavi.net.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import vavi.net.webhook.box.BoxNotificationEndpoint;
import vavi.net.webhook.dropbox.DropBoxNotificationEndpoint;
import vavi.net.webhook.google.GoogleNotificationEndpoint;
import vavi.net.webhook.support.CustomSpringConfigurator;


@ConditionalOnWebApplication
@Configuration
class WebSocketConfig {

    @Bean
    public GoogleNotificationEndpoint googleNotificationEndpoint(@Autowired WebHookService webHookService) {
      return new GoogleNotificationEndpoint(webHookService);
    }

    @Bean
    public DropBoxNotificationEndpoint dropBoxNotificationEndpoint(@Autowired WebHookService webHookService) {
      return new DropBoxNotificationEndpoint(webHookService);
    }

    @Bean
    public BoxNotificationEndpoint boxNotificationEndpoint(@Autowired WebHookService webHookService) {
      return new BoxNotificationEndpoint(webHookService);
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
