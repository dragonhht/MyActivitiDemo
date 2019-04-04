package com.github.dragonhht.activiti.service

import org.activiti.engine.repository.ProcessDefinition
import org.activiti.engine.task.Task

/**
 * 流程基础服务.
 *
 * @author: huang
 * @Date: 2019-4-2
 */
interface BaseService {

    /**
     * 通过Id查询任务
     * @param taskId 任务Id
     * @return 任务
     * @throws Exception
     */
    @Throws(Exception::class)
    fun findTaskById(taskId: String): Task

    /**
     * 通过流程定义Id获取流程定义
     * @param taskId id
     * @return 流程定义
     */
    @Throws(Exception::class)
    fun findProcessDefinitionById(id: String): ProcessDefinition
}
