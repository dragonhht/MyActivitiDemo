package com.github.dragonhht.activiti.service

import org.activiti.engine.repository.Deployment
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task

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
    Deployment deployByZipInputStream(ZipInputStream input, String name, String key)

    /**
     * 通过xml字符串部署流程资源.
     * @param xml xml内容字符串
     * @param name 流程名
     * @param fileName bpmn文件名
     * @return 部署实例
     */
    Deployment deployByXmlString(String xml, String name, String fileName, String key)

    /**
     * 通过bpmn文件输入流部署流程资源
     * @param input bpmn文件输入流
     * @param name 流程名
     * @param fileName bpmn文件名
     * @return 部署实例
     */
    Deployment deployByBpmnInputStream(InputStream input, String name, String fileName, String key)

    /**
     * 部署ClassPath下的bpmn文件.
     * @param filePath ClassPth下BPMN文件路径
     * @param pngName ClassPath下png文件路径
     * @param name 流程名
     * @return 部署实例
     */
    Deployment deployByClassPathWithImg(String filePath, String pngPath, String name, String key)
    Deployment deployByClassPath(String filePath, String name, String key)

    /**
     * 通过部署Id删除部署.
     * @param id 部署Id
     */
    void delDeployById(String id)

    /**
     * 启动流程实例
     * @param id 流程资源key
     * @return 流程实例
     */
    ProcessInstance startProcess(String key)

    /**
     * 完成任务.
     * @param taskId 任务Id
     */
    void complete(String taskId)

    /**
     * 获取待办数据
     * @param useId 用户Id
     * @return 所有待办
     */
    List<Task> getTodoTasks(String userId)
}