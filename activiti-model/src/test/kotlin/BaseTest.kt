import com.github.dragonhht.activiti.ActivitiModelApplication
import com.github.dragonhht.activiti.service.BaseService
import com.github.dragonhht.activiti.service.FlowProcessService
import com.github.dragonhht.activiti.service.SignProcessService
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertNotNull

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-4
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [ActivitiModelApplication::class])
class BaseTest {

    @Autowired
    private lateinit var flowProcessService: FlowProcessService
    @Autowired
    private lateinit var repositoryService: RepositoryService
    @Autowired
    private lateinit var signProcessService: SignProcessService
    @Autowired
    private lateinit var runTimeService: RuntimeService
    @Autowired
    private lateinit var taskService: TaskService
    @Autowired
    private lateinit var baseService: BaseService

    @Before
    fun init() {
        // 会签串行
        flowProcessService.deployByClassPath("processes/system/sign_sequential.bpmn", "sign_sequential")
        flowProcessService.deployByClassPath("processes/system/sign_not_sequential.bpmn", "sign_not_sequential")
    }

    @Test
    fun testBpmn() {
        val filePath = "test/qwe.bpmn"
        val deployment = flowProcessService.deployByClassPath(filePath, "create")
        println("id is ${deployment.id}, name is ${deployment.name}, key is ${deployment.key}")

        val definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.id)
                .singleResult()
        println("key is ${definition.key}")
        /*val key = deployment.key
        flowProcessService.startProcess(key)*/
    }

    @Test
    fun testSign() {
        var taskId: String
        val key = "bill"
        val nodeId = "_9"
        val path = "test/Test2.bpmn"
        val deployment = flowProcessService.deployByClassPath(path, "classpath-bill")
        assertNotNull(deployment)
        println("通过ClassPath部署: id is ${deployment.id}, name is ${deployment.name}, key is ${deployment.key}")
        val vas = mutableMapOf<String, Any>("name" to  "hello", "age" to 12)
        val processInstance = flowProcessService.startProcess(key, vas)
        println("流程实例id: ${processInstance.id}, name: ${processInstance.name}, processDefinitionId: ${processInstance.processDefinitionId}")

        // 获取待办数据
        var userId = "user"
        println("-----------------第一节点----------------------")
        var tasks = flowProcessService.getTodoTasks(userId)
        for (task in tasks) {
            taskId = task.id
            println("task name is ${task.name}, id is ${task.id}, assignee is ${task.assignee}")
            val persons = mutableListOf<String>("person_1", "person_2")
            signProcessService.startSign(persons, task.id, isSequential = false)
            println("ids is : ${flowProcessService.getExecutionVariable(task.executionId, "${taskId}-subTasks")}")
        }

        println("------------------主任务节点------------------------")
        tasks = flowProcessService.getTodoTasks(userId)
        for (task in tasks) {
            taskId = task.id
            println("task name is ${task.name}, id is ${task.id}, assignee is ${task.assignee}")
            //flowProcessService.jumpToNode(taskId, nodeId)
        }

        println("------------------子节点------------------------")
        tasks = flowProcessService.getTodoTasks("person_1")
        for (task in tasks) {
            taskId = task.id
            println("task name is ${task.name}, id is ${task.id}, assignee is ${task.assignee}")
            val persons = mutableListOf<String>("person_4", "person_5")
            signProcessService.startSign(persons, taskId, isSequential = true)
        }

        println("------------------主任务节点------------------------")

        tasks = taskService.createTaskQuery().taskAssignee(userId).list()
        for (task in tasks) {
            taskId = task.id
            println("task name is ${task.name}, id is ${task.id}, assignee is ${task.assignee}")
            flowProcessService.jumpToNode(taskId, nodeId)
        }

        println("------------------第二节点------------------------")
        tasks = flowProcessService.getTodoTasks("person_1")
        for (task in tasks) {
            taskId = task.id
            println("task name is ${task.name}, id is ${task.id}, assignee is ${task.assignee}")
            flowProcessService.complete(taskId)
        }
        println("------------------第三节点------------------------")
        tasks = flowProcessService.getTodoTasks("person_2")
        for (task in tasks) {
            taskId = task.id
            println("task name is ${task.name}, id is ${task.id}, assignee is ${task.assignee}")
            flowProcessService.complete(taskId)
        }

        println("------------------主任务节点------------------------")
        tasks = flowProcessService.getTodoTasks(userId)
        for (task in tasks) {
            taskId = task.id
            println("task name is ${task.name}, id is ${task.id}, assignee is ${task.assignee}")
            println("ids----------------- is : ${flowProcessService.getExecutionVariable(task.executionId, "${taskId}-subTasks")}")
            flowProcessService.complete(taskId)
        }
    }

    @Test
    fun test() {
        val ss = null as MutableList<String>?
        ss?.forEach { println(it) }
    }

    @Test
    fun testParallel() {
        val filePath = "test/parallel.bpmn"
        val processKey = "parallel"
        val userId = "user"
        val deployment = flowProcessService.deployByClassPath(filePath, "parallel")
        assertNotNull(deployment)
        println("deployment id is ${deployment.id}, name is ${deployment.name}, key is ${deployment.key}")

        val instance = flowProcessService.startProcess(processKey)
        assertNotNull(instance)
        println("processInstance id is ${instance.id}, name is ${instance.name}")

        println("---------------------------------------第一次获取任务------------------------------------")
        var tasks = flowProcessService.getTodoTasks(userId)
        tasks?.forEach {
            println("task id is ${it.id}, name is ${it.name}")
            flowProcessService.complete(it.id)
        }

        println("---------------------------------------第二次获取任务------------------------------------")
        tasks = flowProcessService.getTodoTasks(userId)
        var index = 0
        tasks?.forEach {
            println("task id is ${it.id}, name is ${it.name}")
            val persons = listOf(userId, userId)
            if (index == 0) {
                signProcessService.startSign(persons, it.id, isSequential = true)
                //flowProcessService.jumpToNode(it.id, "_22")
            }
            index++
            //flowProcessService.complete(it.id)
        }

        println("---------------------------------------第三次获取任务------------------------------------")
        tasks = flowProcessService.getTodoTasks(userId)
        tasks?.forEach {
            println("task id is ${it.id}, name is ${it.name}")
            flowProcessService.complete(it.id)
        }

        println("---------------------------------------第四次获取任务------------------------------------")
        tasks = flowProcessService.getTodoTasks(userId)
        tasks?.forEach {
            println("task id is ${it.id}, name is ${it.name}")
            flowProcessService.complete(it.id)
        }

        println("---------------------------------------第五次获取任务------------------------------------")
        tasks = flowProcessService.getTodoTasks(userId)
        tasks?.forEach {
            println("task id is ${it.id}, name is ${it.name}")
            flowProcessService.complete(it.id)
        }

        println("---------------------------------------第六次获取任务------------------------------------")
        tasks = flowProcessService.getTodoTasks(userId)
        tasks?.forEach {
            println("task id is ${it.id}, name is ${it.name}")
            flowProcessService.complete(it.id)
        }

        println("---------------------------------------第七次获取任务------------------------------------")
        tasks = flowProcessService.getTodoTasks(userId)
        tasks?.forEach {
            println("task id is ${it.id}, name is ${it.name}")
            flowProcessService.complete(it.id)
        }

        flowProcessService.delDeployById(deployment.id)
        println("-----------------删除部署---------------------------")
    }

    @Test
    fun testNode() {
        val filePath = "test/parallel.bpmn"
        val processKey = "parallel"
        val userId = "user"
        val deployment = flowProcessService.deployByClassPath(filePath, "parallel")
        assertNotNull(deployment)
        val instance = flowProcessService.startProcess(processKey)
        assertNotNull(instance)
    }

}