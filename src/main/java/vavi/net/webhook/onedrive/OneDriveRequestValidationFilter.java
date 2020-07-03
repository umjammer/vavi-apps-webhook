/*
 * https://jp.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests
 */

package vavi.net.webhook.onedrive;

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
 * OneDriveRequestValidationFilter.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 * @see "https://tsmatz.wordpress.com/2016/04/22/onedrive-api-webhook-and-synchronization-delta/"
 * @see "https://stackoverflow.com/a/19957972/6102938"
 */
public class OneDriveRequestValidationFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(OneDriveRequestValidationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        boolean isValidRequest = false;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = new CachedBodyHttpServletRequest((HttpServletRequest) request);
            String validationtoken = httpRequest.getParameter("validationtoken");
            if (validationtoken != null) {
LOG.warn("process verify");
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setContentType("text/plain");
                httpResponse.getWriter().write(validationtoken);
                return;
            } else {
LOG.warn("process normal");
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
