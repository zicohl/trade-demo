package com.trade.demo.interceptor;

import com.trade.demo.annotation.RequiresPermissions;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author honglu
 * @since 2022/1/8
 */
public class AuthorizationAnnotationInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return checkPermissions(request, response, handler);
    }

    private boolean checkPermissions(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //String jsonString = request.getHeader("Authorization-Permissions");
        //List<String> permissions = JSON.parseArray(jsonString, String.class);

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            Class<?> clazz = hm.getBeanType();
            Method m = hm.getMethod();
            try {
                if (clazz != null && m != null) {
                    // 检查类和方法是否有注解
                    RequiresPermissions rc = m.getAnnotation(RequiresPermissions.class);

                    if (rc != null) {
                        // 注解中需要的角色
                        //String[] value = rc.value();
                        //List<String> requireRole = new ArrayList<String>();
                        //Collections.addAll(requireRole, value);

                        // 解码token获取角色
                        //List<String> tokenRole = new ArrayList<String>();
                        //Collections.addAll(tokenRole, permissions);

                        // 进行角色访问的权限控制，只有当前用户是需要的角色才予以访问。
                        //boolean isEquals = tokenRole.containsAll(requireRole);

                        //if (!isEquals) {
                        //response.setStatus(403);
                        //response.getWriter().write("access forbidden");
                        //return false;
                        // }
                    }

                }
            } catch (Exception e) {

            }
        }

        return true;
    }
}
