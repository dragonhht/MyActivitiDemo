package com.github.dragonhht.activiti.service.impl

import org.activiti.engine.RepositoryService
import org.activiti.engine.repository.Deployment
import com.github.dragonhht.activiti.service.FlowProcessService
import org.springframework.stereotype.Service

import javax.annotation.Resource
import java.util.zip.ZipInputStream

/**
 * .
 *
 * @author: huang* @Date: 2019-3-28
 */
@Service
class FlowProcessServiceImpl implements FlowProcessService {

    @Resource
    private RepositoryService repositoryService

    @Override
    Deployment deployByZipInputStream(ZipInputStream input, String name) {
        return repositoryService.createDeployment()
                    .name(name)
                    .addZipInputStream(input)
                    .deploy()
    }

    private String getBpmnFileName(String fileName) {
        if (!fileName.endsWithAny('.bpmn', '.BPMN')) {
            return fileName + '.bpmn'
        }
        return fileName
    }

    @Override
    Deployment deployByXmlString(String xml, String name, String fileName) {
        fileName = getBpmnFileName(fileName)
        return repositoryService.createDeployment()
                    .name(name)
                    .addString(fileName, xml)
                    .deploy()
    }

    @Override
    Deployment deployByBpmnInputStream(InputStream input, String name, String fileName) {
        fileName = getBpmnFileName(fileName)
        return repositoryService.createDeployment()
                    .name(name)
                    .addInputStream(fileName, input)
                    .deploy()
    }

    @Override
    Deployment deployByClassPath(String filePath, String pngName, String name) {
        return repositoryService.createDeployment()
                    .name(name)
                    .addClasspathResource(filePath)
                    .addClasspathResource(pngName)
                    .deploy()
    }

    @Override
    void delDeployById(String id) {
        repositoryService.deleteDeployment(id)
    }
}
