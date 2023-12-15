package com.movie.api.config.intercepors;

import com.movie.api.utils.JwtTokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        // 检查 JWT 令牌是否存在
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // 检查 JWT 令牌的格式
        if (token.split("\\.").length != 3) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        if (JwtTokenUtil.isExpiration(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } else {
            String username = JwtTokenUtil.getUsername(token);
            request.setAttribute("username", username);
            return true;
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
