package com.github.dragonhht.activiti

import com.github.dragonhht.activiti.service.FlowProcessService
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

    }

    @Test
    void delDeploy() {
        def id = '440d6a93-513f-11e9-9ece-00ff43c03ffc'
        flowProcessService.delDeployById(id)
    }

}
