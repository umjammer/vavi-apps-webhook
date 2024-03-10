/*
 * https://jp.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests
 */

package vavi.net.webhook.dropbox;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import vavi.net.webhook.WebHookService;
import vavi.net.webhook.support.CachedBodyHttpServletRequest;


/**
 * DropBoxRequestValidationFilter.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 * @see "https://www.dropbox.com/developers/reference/webhooks"
 * @see "https://github.com/aishraj/dropbox-webhooks-java-example/blob/master/app/controllers/Application.java"
 */
public class DropBoxRequestValidationFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(DropBoxRequestValidationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        boolean isValidRequest = false;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = new CachedBodyHttpServletRequest((HttpServletRequest) request);
            String challenge = httpRequest.getParameter("challenge");
            if (challenge != null) {
LOG.warn("process verify");
                ((HttpServletResponse) response).addHeader("Content-Type", "text/plain");
                ((HttpServletResponse) response).addHeader("X-Content-Type-Options", "nosniff");
                ((HttpServletResponse) response).getWriter().write(challenge);
                return;
            } else {
LOG.warn("process normal");
                isValidRequest = checkDropbox(httpRequest);
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

    boolean checkDropbox(HttpServletRequest httpRequest) throws IOException {
        try {
            String signature = httpRequest.getHeader("X-Dropbox-Signature");
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(System.getenv(WebHookService.DROPBOX_CLIENT_SECRET).getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            byte[] messageBytes = StreamUtils.copyToByteArray(httpRequest.getInputStream());
            byte[] encodedBytes = sha256_HMAC.doFinal(messageBytes);
            return signature.equals(new String(Hex.encodeHex(encodedBytes)));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
LOG.error(e.getMessage(), e);
            return false;
        }
    }
}
