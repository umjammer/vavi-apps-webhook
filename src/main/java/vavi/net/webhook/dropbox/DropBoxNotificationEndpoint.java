
package vavi.net.webhook.dropbox;

import java.io.IOException;

import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import vavi.net.webhook.NotificationEndpoint;
import vavi.net.webhook.WebHookService;
import vavi.net.webhook.support.CustomSpringConfigurator;


@ServerEndpoint(value = "/ws/dropbox",
                configurator = CustomSpringConfigurator.class)
public class DropBoxNotificationEndpoint extends NotificationEndpoint<String> {

    @Autowired
    public DropBoxNotificationEndpoint(WebHookService service) {
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
