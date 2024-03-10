
package vavi.net.webhook.google;

import java.io.IOException;

import jakarta.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.googleapis.notifications.UnparsedNotification;

import vavi.net.webhook.NotificationEndpoint;
import vavi.net.webhook.WebHookService;
import vavi.net.webhook.google.GoogleJsonCodec.GoogleJsonDecoder;
import vavi.net.webhook.google.GoogleJsonCodec.GoogleJsonEncoder;
import vavi.net.webhook.support.CustomSpringConfigurator;


@ServerEndpoint(value = "/ws/google",
                configurator = CustomSpringConfigurator.class,
                decoders = GoogleJsonDecoder.class,
                encoders = GoogleJsonEncoder.class)
public class GoogleNotificationEndpoint extends NotificationEndpoint<UnparsedNotification> {

    @Autowired
    public GoogleNotificationEndpoint(WebHookService service) {
        super(service);
    }

    public void sendNotification(UnparsedNotification notification) throws IOException {
        if (getSession() != null) {
            getSession().getAsyncRemote().sendObject(notification);
        } else {
            LOG.warn("no session");
        }
    }
}
