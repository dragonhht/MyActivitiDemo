package com.github.dragonhht.activiti.utils

import java.util.*

/**
 * 创建Id工具类.
 *
 * @author: huang
 * @Date: 2019-4-2
 */
class IDGenerator {

    companion object {

        /**
         * UUID以时间戳生成Id
         * @return Id
         */
        fun getId(): String {
            return "${System.currentTimeMillis()}${UUID.randomUUID().mostSignificantBits and  Long.MAX_VALUE}"
        }
    }

}


