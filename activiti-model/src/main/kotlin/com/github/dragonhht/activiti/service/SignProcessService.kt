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
     */
    fun startSign(assignees: List<String>, taskId: String, variables: Map<String, Any> = mutableMapOf())

}
