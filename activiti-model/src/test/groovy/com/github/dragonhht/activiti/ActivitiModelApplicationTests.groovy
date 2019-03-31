package com.github.dragonhht.activiti

import com.github.dragonhht.activiti.service.FlowProcessService
import lombok.extern.slf4j.Slf4j
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import java.util.zip.ZipInputStream

@RunWith(SpringRunner)
@SpringBootTest
class ActivitiModelApplicationTests {

    @Autowired
    FlowProcessService flowProcessService


    /**
     * 部署接口测试.
     */
    @Test
    void testDeploy() {
        // 通过zip包部署
        def zipUrl = 'C:\\Users\\huang\\Desktop\\Test.zip'
        def input = new ZipInputStream(new File(zipUrl).newInputStream())
        def deployment = flowProcessService.deployByZipInputStream(input, 'zip-bill')
        assert deployment != null
        println "通过zip部署: id is ${deployment.id}, name is ${deployment.name}, key is ${deployment.key}"

        flowProcessService.delDeployById(deployment.id)
        deployment = null

        // 通过xml字符串部署
        def xmlUrl = 'C:\\Users\\huang\\Desktop\\Test.xml'
        def xmlStr = new File(xmlUrl).text
        deployment = flowProcessService.deployByXmlString(xmlStr, 'xml-bill', 'xml-bill')
        assert deployment != null
        println "通过xml字符串部署: id is ${deployment.id}, name is ${deployment.name}, key is ${deployment.key}"

        flowProcessService.delDeployById(deployment.id)
        deployment = null

        // 通过bpmn文件流部署
        def bpmnUrl = 'D:\\my_work_spance\\idea_workspance\\MyActivitiDemo\\activiti-model\\src\\main\\resources\\test\\Test.bpmn'
        def bpmnInput = new File(bpmnUrl).newInputStream()
        deployment = flowProcessService.deployByBpmnInputStream(bpmnInput, 'bpmn-input-bill', 'Test')
        assert deployment != null
        println "通过bpmn文件流部署: id is ${deployment.id}, name is ${deployment.name}, key is ${deployment.key}"

        flowProcessService.delDeployById(deployment.id)
        deployment = null

        // 通过ClassPath
        def path = 'test/Test.bpmn'
        deployment = flowProcessService.deployByClassPath(path, 'classpath-boll')
        assert deployment != null
        println "通过ClassPath部署: id is ${deployment.id}, name is ${deployment.name}, key is ${deployment.key}"

        flowProcessService.delDeployById(deployment.id)
    }

    @Test
    void delDeploy() {
        def id = '440d6a93-513f-11e9-9ece-00ff43c03ffc'
        flowProcessService.delDeployById(id)
    }

    @Test
    void testTask() {
        def key = 'bill'
        def path = 'test/Test2.bpmn'
        def deployment = flowProcessService.deployByClassPath(path, 'classpath-bill')
        assert deployment != null
        println "通过ClassPath部署: id is ${deployment.id}, name is ${deployment.name}, key is ${deployment.key}"

        def processInstance = flowProcessService.startProcess(key)

        println "流程实例id: ${processInstance.id}, name: ${processInstance.name}, processDefinitionId: ${processInstance.processDefinitionId}"

        // 获取待办数据
        def userId = 'user'
        def tasks = flowProcessService.getTodoTasks(userId)
        for (task in tasks) {
            println "task name is ${task.name}, id is ${task.id}, assignee is ${task.assignee}"
            flowProcessService.complete(task.id)
        }

        println '---------------------------------------'
        tasks = flowProcessService.getTodoTasks(userId)
        for (task in tasks) {
            println "task name is ${task.name}, id is ${task.id}, assignee is ${task.assignee}"
            flowProcessService.complete(task.id)
        }

        flowProcessService.delDeployById(deployment.id)
    }

}
