package com.github.dragonhht.activiti.controller

import com.github.dragonhht.activiti.service.FlowProcessService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

import javax.annotation.Resource
import java.util.zip.ZipInputStream

/**
 * 测试Controller.
 *
 * @author: huang* @Date: 2019-3-28
 */
@Controller
@RequestMapping("/test")
class TestController {

    @Resource
    FlowProcessService flowProcessService

    @PostMapping("/upload")
    @ResponseBody
    void upload(@RequestParam("file") MultipartFile file) {
        def deployment = flowProcessService.deployByZipInputStream(new ZipInputStream(file.inputStream), 'bill')
        println "上传成功， id is ${deployment.id}, name is ${deployment.name}"
        flowProcessService.delDeployById(deployment.id)
    }

    @GetMapping("/fileEdit")
    String fileEdit(Model model) {
        model.addAttribute('formAction', '/test/upload')
        model.addAttribute('key', 'upload')
        return 'components/file_edit'
    }

    @GetMapping("/bpmn/view")
    String view() {
        return '../static/dist/index.html'
    }

}
