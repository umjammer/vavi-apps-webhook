/*
 * https://jp.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests
 */

package vavi.net.webhook.microsoft;

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

import vavi.net.webhook.support.CachedBodyHttpServletRequest;


/**
 * MicrosoftRequestValidationFilter.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 * @see "https://tsmatz.wordpress.com/2016/04/22/onedrive-api-webhook-and-synchronization-delta/"
 * @see "https://docs.microsoft.com/ja-jp/graph/webhooks#notification-endpoint-validation"
 */
public class MicrosoftRequestValidationFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(MicrosoftRequestValidationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        boolean isValidRequest = false;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = new CachedBodyHttpServletRequest((HttpServletRequest) request);
            String validationtoken = httpRequest.getParameter("validationToken");
            if (validationtoken != null) {
LOG.info("process verify");
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setContentType("text/plain");
                httpResponse.getWriter().write(validationtoken);
                return;
            } else {
LOG.info("process normal");
                isValidRequest = checkOneDrive(httpRequest);
                request = httpRequest;
            }
        }

        if (isValidRequest) {
            chain.doFilter(request, response);
        } else {
LOG.warn("rejected by filter");
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    boolean checkOneDrive(HttpServletRequest httpRequest) throws IOException {
        return true;
    }
}
