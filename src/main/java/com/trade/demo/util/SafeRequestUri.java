package com.trade.demo.util;

import org.apache.tomcat.util.http.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author honglu
 * @since 2021/4/8
 */
public class SafeRequestUri {
    private SafeRequestUri() {

    }

    public static String getSaftRequestUri(String requestUri) throws UnsupportedEncodingException {
        if (requestUri == null) {
            return null;
        } else {
            requestUri = URLDecoder.decode(requestUri, "utf-8");
            int nextSemiColon = requestUri.indexOf(';');
            if (nextSemiColon != -1) {
                StringBuffer result = new StringBuffer(requestUri.length());
                result.append(requestUri.substring(0, nextSemiColon));

                while (true) {
                    int nextSlash = requestUri.indexOf(47, nextSemiColon);
                    if (nextSlash == -1) {
                        break;
                    }

                    nextSemiColon = requestUri.indexOf(';', nextSlash);
                    if (nextSemiColon == -1) {
                        result.append(requestUri.substring(nextSlash));
                        break;
                    }

                    result.append(requestUri.substring(nextSlash, nextSemiColon));

                }
                requestUri = result.toString();
            }

            return RequestUtil.normalize(requestUri);
        }
    }

    public static String getSaftRequestUri(HttpServletRequest request) throws UnsupportedEncodingException {
        return getSaftRequestUri(request.getRequestURI());
    }
}
