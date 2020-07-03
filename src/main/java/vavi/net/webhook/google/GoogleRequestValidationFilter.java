/*
 * https://jp.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests
 */

package vavi.net.webhook.google;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vavi.net.webhook.WebHookService;


/**
 * GoogleRequestValidationFilter.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 */
public class GoogleRequestValidationFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleRequestValidationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        boolean isValidRequest = false;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            isValidRequest = checkGoogle(httpRequest);
        }

        if (isValidRequest) {
            chain.doFilter(request, response);
        } else {
LOG.warn("rejected by filter");
            ((HttpServletResponse)response).sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    boolean checkGoogle(HttpServletRequest httpRequest) {
        String channelToken = httpRequest.getHeader("X-Goog-Channel-Token");
        return channelToken != null && channelToken.equals(System.getenv(WebHookService.VAVI_APPS_WEBHOOK_SECRET));
    }
}
