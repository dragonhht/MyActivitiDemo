package com.github.dragonhht.activiti.service.impl

import com.github.dragonhht.activiti.model.Node
import com.github.dragonhht.activiti.service.BaseService
import org.activiti.bpmn.model.SequenceFlow
import org.activiti.bpmn.model.UserTask
import org.activiti.engine.RepositoryService
import org.activiti.engine.TaskService
import org.activiti.engine.impl.RepositoryServiceImpl
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity
import org.activiti.engine.impl.persistence.entity.SuspensionState
import org.activiti.engine.impl.persistence.entity.TaskEntity
import org.activiti.engine.repository.ProcessDefinition
import org.activiti.engine.task.Task
import org.springframework.stereotype.Service

import javax.annotation.Resource

/**
 * 流程基础服务实现类.
 *
 * @author: huang
 * @Date: 2019-4-2
 */
@Service("activitiBaseService")
open class BaseServiceImpl: BaseService {

    @Resource
    private lateinit var taskService: TaskService
    @Resource
    private lateinit var repositoryService: RepositoryService

    @Throws(Exception::class)
    override fun findTaskById(taskId: String): Task {
        return taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult() ?: throw RuntimeException("任务实例未找到!")
    }

    @Throws(Exception::class)
    override fun findProcessDefinitionById(id: String): ProcessDefinition {
        return (repositoryService as RepositoryServiceImpl)
                .getDeployedProcessDefinition(id) ?: throw RuntimeException("流程定义未找到!")
    }

    override fun setTaskSuspensionState(taskId: String, state: SuspensionState) {
        var task = findTaskById(taskId) as TaskEntity
        task.suspensionState = state.stateCode
        taskService.saveTask(task)
    }

    override fun setTaskSuspensionState(task: Task, state: SuspensionState) {
        var task = task as TaskEntity
        task.suspensionState = state.stateCode
        taskService.saveTask(task)
    }

    override fun getTaskSuspensionState(taskId: String): Boolean {
        var task = findTaskById(taskId)
        return task.isSuspended
    }

    override fun getProcessDefinitionTaskNodeInfo(id: String): List<Node> {
        val list = mutableListOf<Node>()
        val bpmModel = repositoryService.getBpmnModel(id)
        val flowElements = bpmModel?.mainProcess?.flowElements
        flowElements?.forEach { node ->
            if (node is UserTask) {
                list.add(Node(node.id, node.name, node::class))
            }
        }
        return list
    }

    override fun getProcessDefinitionAllNodeInfo(id: String): List<Node> {
        val list = mutableListOf<Node>()
        val bpmModel = repositoryService.getBpmnModel(id)
        val flowElements = bpmModel?.mainProcess?.flowElements
        flowElements?.forEach { node ->
            if (node is SequenceFlow) {
                list.add(Node(node.id, node.toString(), node::class))
            } else {
                list.add(Node(node.id, node.name, node::class))
            }

        }
        return list
    }
}
