import com.github.dragonhht.activiti.ActivitiModelApplication
import com.github.dragonhht.activiti.service.FlowProcessService
import org.activiti.engine.RepositoryService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.Paths

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

}