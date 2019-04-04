package com.github.dragonhht.activiti.i18n

import org.springframework.lang.Nullable
import org.springframework.util.StringUtils
import org.springframework.web.servlet.LocaleResolver
import java.util.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 国际化.
 *
 * @author: huang
 * @Date: 2019-3-28
 */
class MyLocaleResolver: LocaleResolver {

    override fun resolveLocale(httpServletRequest: HttpServletRequest): Locale {
        val l = httpServletRequest.getParameter("lang")
        var locale = Locale.getDefault()
        if (!StringUtils.isEmpty(l)) {
            val split = l.split("_")
            locale = Locale(split[0], split[1])
        }
        return locale
    }

    override fun setLocale(httpServletRequest: HttpServletRequest, @Nullable httpServletResponse: HttpServletResponse, @Nullable locale: Locale) {

    }
}
