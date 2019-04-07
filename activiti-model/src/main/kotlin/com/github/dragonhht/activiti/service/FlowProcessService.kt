package com.github.dragonhht.activiti.service

import org.activiti.engine.repository.Deployment
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
import java.io.InputStream

import java.util.zip.ZipInputStream

/**
 * 流程处理主要接口.
 *
 * @author: huang
 * @Date: 2019-3-28
 */
interface FlowProcessService {

    /**
     * 通过zip包部署流程资源
     * @param input zip包的输入流
     * @param name 流程名
     * @return 部署实例
     */
    fun deployByZipInputStream(input: ZipInputStream, name: String): Deployment

    /**
     * 通过xml字符串部署流程资源.
     * @param xml xml内容字符串
     * @param name 流程名
     * @param fileName bpmn文件名
     * @return 部署实例
     */
    fun deployByXmlString(xml: String, name: String, fileName: String): Deployment

    /**
     * 通过bpmn文件输入流部署流程资源
     * @param input bpmn文件输入流
     * @param name 流程名
     * @param fileName bpmn文件名
     * @return 部署实例
     */
    fun deployByBpmnInputStream(input: InputStream, name: String, fileName: String): Deployment

    /**
     * 部署ClassPath下的bpmn文件.
     * @param filePath ClassPth下BPMN文件路径
     * @param pngName ClassPath下png文件路径
     * @param name 流程名
     * @return 部署实例
     */
    fun deployByClassPathWithImg(filePath: String, pngPath: String, name: String): Deployment
    fun deployByClassPath(filePath: String, name: String): Deployment

    /**
     * 通过部署Id删除部署.
     * @param id 部署Id
     */
    fun delDeployById(id: String)

    /**
     * 启动流程实例
     * @param id 流程资源key
     * @return 流程实例
     */
    fun startProcess(key: String, variables: Map<String, Any> = mutableMapOf()): ProcessInstance

    /**
     * 完成任务.
     * @param taskId 任务Id
     */
    fun complete(taskId: String, variables: Map<String, Any> = mutableMapOf())

    /**
     * 获取待办数据
     * @param useId 用户Id
     * @return 所有待办
     */
    fun getTodoTasks(userId: String): List<Task>

    /**
     * 获取任务参数.
     * @param taskId 任务ID
     * @param key 参数的键
     * @return 参数值
     */
    fun getVariable(taskId: String, key: String): Any

    /**
     * 获取任务参数
     * @param taskId 任务ID
     * @return 任务的所有参数
     */
    fun getVariables(taskId: String): Map<String, Any>

    /**
     * 设置签收人.
     * @param taskId 任务Id
     * @param assignId 签收人
     */
    fun setAssign(taskId: String, assignId: String)

    /**
     * 跳转至指定节点(不会设置历史处理人).
     * @param taskId 当前任务ID
     * @param nodeId 跳转到的节点Id
     */
    fun jumpToNode(taskId: String, nodeId: String, variables: Map<String, Any> = mutableMapOf())

    /**
     * 跳转至指定节点(已经处理的节点会将历史处理人设置为处理人)
     * @param task 任务
     * @param nodeId 跳转到的节点Id
     */
    fun jumpToNode(task: Task, nodeId: String, variables: Map<String, Any> = mutableMapOf())

    /**
     * 跳转任意节点后将处理人设置为历史处理人.
     * @param processInstanceId 流程实例id
     * @param definitionKey 流程定义key
     */
    fun setBackTaskDealer(processInstanceId: String, definitionKey: String)

    /**
     * 将流程挂起.
     * @param id 流程实例Id
     */
    fun suspendProcessInstanceById(id: String)

    /**
     * 将流程激活.
     * @param id 流程实例Id
     */
    fun activeProcessInstanceById(id: String)
}