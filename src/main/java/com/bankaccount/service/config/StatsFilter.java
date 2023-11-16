package com.bankaccount.service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

@Component
public class StatsFilter extends OncePerRequestFilter implements Ordered {

    private static final Logger log = LogManager.getLogger(StatsFilter.class);
    private final int order = Ordered.LOWEST_PRECEDENCE - 8;
    @Override
    public void destroy() {
        log.info("Destroy StatsFilter");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } catch (IOException | ServletException ex) {

        } finally {
            
            String paramsAndValues = "";
            String uri = wrappedRequest.getRequestURI();
            
            if(null != wrappedRequest.getParameterNames() && wrappedRequest.getParameterNames().hasMoreElements()){
                List<String> paramNames = Collections.list(wrappedRequest.getParameterNames());
                for(String parameter : paramNames){
                    paramsAndValues += parameter + "=" + wrappedRequest.getParameter(parameter) + "&";
                }
                paramsAndValues = "?" + paramsAndValues.substring(0, paramsAndValues.length() - 1);
            }

            if( !uri.contains("/swagger-ui/") && !uri.contains("/v3/api-docs") ){
                log.info("##########################################################################################");
                log.info("# Http method: {}", wrappedRequest.getMethod());
                log.info("# Request uri: {}", uri + paramsAndValues);
                log.info("# Request body : {}", getRequestBodyAsString(wrappedRequest));
                log.info("# Response status code : {}", wrappedResponse.getStatus());
                log.info("# Response body : {}", getResponseBodyAsString(wrappedResponse));
                log.info("##########################################################################################");
            }
            
            //call this method to return the response
            wrappedResponse.copyBodyToResponse();
        }

    }

    @Override
    public int getOrder() {
        return order;
    }

    private String getRequestBodyAsString(ContentCachingRequestWrapper request) {
        
        // wrap request to make sure we can read the body of the request (otherwise it will be consumed by the actual request handler)
        String payload = null;
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    logger.error(ex.getMessage());
                }
            }
        }
        
        return payload;
    }
    
    
    
    
    
    private String getResponseBodyAsString(ContentCachingResponseWrapper response) {

        String payload = null;
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    logger.error(ex.getMessage());
                }
            }
        }

        return payload;
    }
}