package activiti

import hht.dragon.MyActivitiDemoApplication
import hht.dragon.activiti.FlowProcessService
import org.activiti.engine.RepositoryService
import org.activiti.engine.TaskService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * .
 *
 * @author: huang* @Date: 2019-3-27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyActivitiDemoApplication.class)
class ServiceTest {

    @Autowired
    def FlowProcessService flowProcessService
    @Autowired
    def RepositoryService repositoryService
    @Autowired
    def TaskService taskService

    @Test
    void testDeloy() {
        def filePath = 'D:\\my_work_spance\\idea_workspance\\MyActivitiDemo\\src\\main\\resources\\processes\\study\\leav-bill.bpmn'
        def fileName = 'leav-bill.bpmn'
        def input = new FileInputStream(new File(filePath))
        flowProcessService.deploy(input, 'leave-bill', fileName)
    }

    @Test
    void testStringDeploy() {
        def filePath = 'D:\\my_work_spance\\idea_workspance\\MyActivitiDemo\\src\\main\\resources\\processes\\study\\leav-bill.bpmn'
        def text = new File(filePath).text
        flowProcessService.deployByStr(text, 'leav-bill', 'leave-bill.bpmn')
    }

    @Test
    void testDelDeploy() {
        def deployList = repositoryService.createDeploymentQuery()
                .deploymentName("leave-bill")
                .list()
        for (deploy in deployList) {
            def id = deploy.id, name = deploy.name
            println "id is $id, name is $name"
            flowProcessService.delDeploy(id)
        }

        println '删除后....'
        deployList = repositoryService.createDeploymentQuery()
                .deploymentName("leave-bill")
                .list()
        for (deploy in deployList) {
            println "id is ${deploy.id}, name is ${deploy.name}"
        }
    }

    /**
     * 获取任务
     */
    @Test
    void testGetTask() {
        def userId = 'deptLeader'
        // 获取已签收或直接分配给指定用户的任务
        println "当前处理人为 $userId 的任务"
        def doingTasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .list()
        for (task in doingTasks) {
            println "name is ${task.name}"
        }

        println "等待签收的任务"
        // 获取等待签收的任务
        def waitingTasks = taskService.createTaskQuery()
                .taskCandidateUser(userId)
                .list()
        for (task in waitingTasks) {
            println "name is ${task.name}"
        }

        println "获取所有待办任务"
        // 获取所有待办任务
        def todoTasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned(userId)
                .list()
        for (task in todoTasks) {
            println "name is ${task.name}"
        }
    }

}
