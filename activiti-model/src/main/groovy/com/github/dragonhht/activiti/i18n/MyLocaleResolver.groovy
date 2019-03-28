package com.github.dragonhht.activiti.i18n

import org.springframework.lang.Nullable
import org.springframework.util.StringUtils
import org.springframework.web.servlet.LocaleResolver

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * .
 *
 * @author: huang* @Date: 2019-3-28
 */
class MyLocaleResolver implements LocaleResolver {
    @Override
    Locale resolveLocale(HttpServletRequest httpServletRequest) {
        def l = httpServletRequest.getParameter("lang")
        def locale = Locale.getDefault()
        if (!StringUtils.isEmpty(l)) {
            String[] split = l.split("_")
            locale = new Locale(split[0], split[1])
        }
        return locale
    }

    @Override
    void setLocale(HttpServletRequest httpServletRequest, @Nullable HttpServletResponse httpServletResponse, @Nullable Locale locale) {

    }
}
