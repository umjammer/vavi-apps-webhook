/*
 * https://jp.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests
 */

package vavi.net.webhook.onedrive;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        boolean isValidRequest = false;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = new CachedBodyHttpServletRequest((HttpServletRequest) request);
            String validationtoken = httpRequest.getParameter("validationtoken");
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

    @Override
    public void destroy() {

    }

    boolean checkOneDrive(HttpServletRequest httpRequest) throws IOException {
        return true;
    }
}
