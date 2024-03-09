/*
 * https://jp.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests
 */

package vavi.net.webhook.box;

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
import org.springframework.util.StreamUtils;

import com.box.sdk.BoxWebHookSignatureVerifier;

import vavi.net.webhook.WebHookService;
import vavi.net.webhook.support.CachedBodyHttpServletRequest;


/**
 * BoxRequestValidationFilter.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 */
public class BoxRequestValidationFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(BoxRequestValidationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        boolean isValidRequest = false;
        if (request instanceof HttpServletRequest) {
            request = new CachedBodyHttpServletRequest((HttpServletRequest) request);
            isValidRequest = checkBox((HttpServletRequest) request);
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

    boolean checkBox(HttpServletRequest httpRequest) throws IOException {
        String primaryKey = System.getenv(WebHookService.BOX_WEBHOOK_PRIMARY_KEY);
        String secondaryKey = null;
        byte[] messagePayload = StreamUtils.copyToByteArray(httpRequest.getInputStream());
        BoxWebHookSignatureVerifier verifier = new BoxWebHookSignatureVerifier(primaryKey, secondaryKey);
        return verifier.verify(
            httpRequest.getHeader("BOX-SIGNATURE-VERSION"),
            httpRequest.getHeader("BOX-SIGNATURE-ALGORITHM"),
            httpRequest.getHeader("BOX-SIGNATURE-PRIMARY"),
            httpRequest.getHeader("BOX-SIGNATURE-SECONDARY"),
            new String(messagePayload),
            httpRequest.getHeader("BOX-DELIVERY-TIMESTAMP")
        );
    }
}
