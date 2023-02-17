package com.larry.fc.finalproject.api.config;

import com.larry.fc.finalproject.api.dto.AuthUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class FakeAuthUserResolver implements HandlerMethodArgumentResolver { //개발환경에서만 써야 한다

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return AuthUser.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String userIdStr = webRequest.getParameter("userId");
        if(userIdStr == null){
            return new AuthUserResolver().resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        }
        return AuthUser.of(Long.parseLong(userIdStr));    //userId 가 있으면 이 아이디를 가지고 AuthUser 만들어준다
    }
}
