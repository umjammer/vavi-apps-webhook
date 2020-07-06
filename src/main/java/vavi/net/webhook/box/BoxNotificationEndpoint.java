
package vavi.net.webhook.box;

import java.io.IOException;

import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import vavi.net.webhook.NotificationEndpoint;
import vavi.net.webhook.WebHookService;
import vavi.net.webhook.support.CustomSpringConfigurator;


@ServerEndpoint(value = "/ws/box",
                configurator = CustomSpringConfigurator.class)
public class BoxNotificationEndpoint extends NotificationEndpoint<String> {

    @Autowired
    public BoxNotificationEndpoint(WebHookService service) {
        super(service);
    }

    public void sendNotification(String notification) throws IOException {
        if (getSession() != null) {
            getSession().getAsyncRemote().sendText(notification);
        } else {
            LOG.warn("no session");
        }
    }
}
