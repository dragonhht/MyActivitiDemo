package com.github.dragonhht.activiti.service

/**
 * 会签流程服务.
 *
 * @author: huang
 * @Date: 2019-4-2
 */
interface SignProcessService {

    /**
     * 启动会签任务
     * @param assignees 会签处理人
     * @param taskId 任务Id
     * @param isSequential 是否串行
     * @param variables 参数
     */
    fun startSign(assignees: List<String>, taskId: String,
                  isSequential: Boolean = false, variables: MutableMap<String, Any> = mutableMapOf())

}
