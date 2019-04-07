package com.github.dragonhht.activiti.service.impl

import com.github.dragonhht.activiti.commands.FreeJumpCommand
import com.github.dragonhht.activiti.commands.ResetTaskAssignee
import com.github.dragonhht.activiti.commands.UpdateHiTaskReasonCommand

import com.github.dragonhht.activiti.service.FlowProcessService
import org.activiti.engine.HistoryService
import org.activiti.engine.ManagementService
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.history.HistoricTaskInstance
import org.activiti.engine.repository.Deployment
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
import org.apache.batik.svggen.font.table.Table.name
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.io.InputStream

import javax.annotation.Resource
import java.util.zip.ZipInputStream

/**
 * .
 *
 * @author: huang
 * @Date: 2019-3-28
 */
@Service("flowProcessService")
open class FlowProcessServiceImpl: FlowProcessService {

    @Resource
    private lateinit var repositoryService: RepositoryService
    @Resource
    private lateinit var runtimeService: RuntimeService
    @Resource
    private lateinit var taskService: TaskService
    @Autowired
    private lateinit var managementService: ManagementService
    @Autowired
    private lateinit var historyService: HistoryService

    override fun deployByZipInputStream(input: ZipInputStream, name: String): Deployment {
        return repositoryService.createDeployment()
                .name(name)
                .addZipInputStream(input)
                .deploy()
    }

    private fun getBpmnFileName(fileName: String): String {
        if (!fileName.endsWith(".bpmn") && !fileName.endsWith(".BPMN")) {
            return "$fileName.bpmn"
        }
        return fileName
    }

    override fun deployByXmlString(xml: String, name: String, fileName: String): Deployment {
        val fileName = getBpmnFileName(fileName)
        return repositoryService.createDeployment()
                .name(name)
                .addString(fileName, xml)
                .deploy()
    }

    override fun deployByBpmnInputStream(input: InputStream, name: String, fileName: String): Deployment {
        val fileName = getBpmnFileName(fileName)
        return repositoryService.createDeployment()
                .name(name)
                .addInputStream(fileName, input)
                .deploy()
    }

    override fun deployByClassPathWithImg(filePath: String, pngPath: String, name: String): Deployment {
        return repositoryService.createDeployment()
                .name(name)
                .addClasspathResource(filePath)
                .addClasspathResource(pngPath)
                .deploy()
    }

    override fun deployByClassPath(filePath: String, name: String): Deployment {
        return repositoryService.createDeployment()
                .name(name)
                .addClasspathResource(filePath)
                .deploy()
    }

    override fun delDeployById(id: String) {
        repositoryService.deleteDeployment(id)
    }

    override fun startProcess(key: String, variables: Map<String, Any>): ProcessInstance {
        return runtimeService.startProcessInstanceByKey(key, variables)
    }

    override fun complete(taskId: String, variables: Map<String, Any>) {
        taskService.complete(taskId, variables)
        managementService.executeCommand(UpdateHiTaskReasonCommand(taskId, "completed"))
        managementService.executeCommand(ResetTaskAssignee(taskId))
    }

    override fun getTodoTasks(userId: String): List<Task> {
        return taskService.createTaskQuery()
                .taskAssignee(userId)
                .active()
                .list()
    }

    override fun getVariable(taskId: String, key: String): Any {
        return taskService.getVariable(taskId, key)
    }

    override fun getVariables(taskId: String): Map<String, Any> {
        return taskService.getVariables(taskId)
    }

    override fun setAssign(taskId: String, assignId: String) {
        taskService.setAssignee(taskId, assignId)
    }

    override fun jumpToNode(taskId: String, nodeId: String, variables: Map<String, Any>) {
        managementService.executeCommand(FreeJumpCommand(taskId, nodeId, variables))
    }

    override fun jumpToNode(task: Task, nodeId: String, variables: Map<String, Any>) {
        managementService.executeCommand(FreeJumpCommand(task.id, nodeId, variables))
        setBackTaskDealer(task.processInstanceId, nodeId)
    }

    override fun setBackTaskDealer(processInstanceId: String, definitionKey: String) {
        val list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(definitionKey)
                .taskDeleteReason("completed")
                .orderByTaskCreateTime().desc()
                .list()
        var historicTask: HistoricTaskInstance
        if (list != null && list.size > 0) {
            historicTask = list[0]
            // 查询回退后的节点正在运行的任务
            val taskList = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskDefinitionKey(definitionKey)
                    .active()
                    .list()
            // 同一节点下有多个任务，则认定为会签任务
            if (taskList != null && taskList.size > 1) {
                taskList.forEach{
                    // 设置会签任务处理人（处理人顺序不管）
                    taskService.setAssignee(it.id, it.assignee)
                }
            } else {
                val taskNew = taskList[0]
                // 顺序会签流程变量处理人
                val variable = runtimeService.getVariable(taskNew.executionId, "countersign") as String
                if (!StringUtils.isEmpty(variable)) {
                    // 设置下个顺序会签处理人
                    setAssign(taskNew.id, variable)
                } else {
                    // 设置一般回退任务处理人
                    taskService.setAssignee(taskNew.id, historicTask.assignee)
                }
            }
        }
    }

    override fun suspendProcessInstanceById(id: String) {
        runtimeService.suspendProcessInstanceById(id)
    }

    override fun activeProcessInstanceById(id: String) {
        runtimeService.activateProcessInstanceById(id)
    }
}
