package com.github.dragonhht.activiti.utils

/**
 * 创建Id工具类.
 *
 * @author: huang
 * @Date: 2019-4-2
 */
class IDGenerator {

    /**
     * UUID以时间戳生成Id
     * @return Id
     */
    static String getId() {
        return "${System.currentTimeMillis()}${UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE}"
    }

}


