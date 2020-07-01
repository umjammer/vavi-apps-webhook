/*
 * https://jp.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests
 */

package vavi.net.webhook;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * RequestValidationFilter.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/07/01 umjammer initial version <br>
 */
public class RequestValidationFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        boolean isValidRequest = false;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // Concatenates the request URL with the query string
            String pathAndQueryUrl = getRequestUrlAndQueryString(httpRequest);
            // Extracts only the POST parameters and converts the parameters Map type
            Map<String, String> postParams = extractPostParams(httpRequest);
            String signatureHeader = httpRequest.getHeader("X-Twilio-Signature");

        }

        if(isValidRequest) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse)response).sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {
        // Nothing to do
    }

    private Map<String, String> extractPostParams(HttpServletRequest request) {
        String queryString = request.getQueryString();
        Map<String, String[]> requestParams = request.getParameterMap();
        List<String> queryStringKeys = getQueryStringKeys(queryString);

        return requestParams.entrySet().stream()
                .filter(e -> !queryStringKeys.contains(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()[0]));
    }

    private List<String> getQueryStringKeys(String queryString) {
        if(queryString == null || queryString.length() == 0) {
            return Collections.emptyList();
        } else {
            return Arrays.stream(queryString.split("&"))
                    .map(pair -> pair.split("=")[0])
                    .collect(Collectors.toList());
        }
    }

    private String getRequestUrlAndQueryString(HttpServletRequest request) {
        String queryString = request.getQueryString();
        String requestUrl = request.getRequestURL().toString();
        if(queryString != null && queryString != "") {
            return requestUrl + "?" + queryString;
        }
        return requestUrl;
    }
}
