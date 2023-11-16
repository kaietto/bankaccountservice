package com.bankaccount.service.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Claudio Menin
 * @description: This method is responsible for handling CORS (Cross-Origin Resource Sharing) in the application
 */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {
    
    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        final HttpServletRequest request = (HttpServletRequest) req;
        // Set the 'Access-Control-Allow-Origin' header to allow requests from any origin
        response.setHeader("Access-Control-Allow-Origin", "*");
        // Check if request is an OPTIONS request (preflight request) and set additional CORS headers
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers",
                    "Origin, Content-Type, content-type, Accept, Authorization, access-control-allow-origin,  X-Requested-With");
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }
        // If it's not an OPTIONS request, continue with the filter chain
        if (!"OPTIONS".equals(request.getMethod())) {
            chain.doFilter(req, res);
        } else {
            response.setStatus(200);
        }
    }
    // Initialization method for the Filter (not used in this case)
    @Override
    public void init(final FilterConfig filterConfig) {}
    // Destruction method for the Filter (not used in this case)
    @Override
    public void destroy() {}

}

