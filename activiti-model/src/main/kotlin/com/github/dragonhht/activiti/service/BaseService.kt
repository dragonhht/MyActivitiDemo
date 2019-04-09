package com.github.dragonhht.activiti.service

import org.activiti.engine.impl.persistence.entity.SuspensionState
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

    /**
     * 设置任务挂起状态.
     * @param taskId 任务Id
     * @param state 状态
     */
    @Throws(Exception::class)
    fun setTaskSuspensionState(taskId: String, state: SuspensionState)

    /**
     * 设置任务挂起状态.
     * @param taskId 任务
     * @param state 状态
     */
    @Throws(Exception::class)
    fun setTaskSuspensionState(task: Task, state: SuspensionState)

    /**
     * 获取任务挂起状态.
     * @param taskId 任务id
     * @return true为挂起，false为非挂起状态
     */
    @Throws(Exception::class)
    fun getTaskSuspensionState(taskId: String): Boolean
}
