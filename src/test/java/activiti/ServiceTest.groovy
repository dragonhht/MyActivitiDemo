package activiti

import hht.dragon.MyActivitiDemoApplication
import hht.dragon.activiti.FlowProcessService
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
    def FlowProcessService flowProcessService;

    @Test
    def testDeloy() {
        def filePath = 'D:\\my_work_spance\\idea_workspance\\MyActivitiDemo\\src\\main\\resources\\processes\\study\\leav-bill.bpmn'
        def fileName = 'leav-bill.bpmn'

    }

}
