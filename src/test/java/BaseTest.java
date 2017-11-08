import hht.dragon.MyActivitiDemoApplication;
import hht.dragon.activiti.FlowProcessService;
import hht.dragon.entity.LeaveBill;
import hht.dragon.utils.HibernateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Description.
 *
 * @author: huang
 * Date: 17-11-5
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyActivitiDemoApplication.class)
public class BaseTest {

    @Autowired
    private HibernateUtils hibernateUtils;
    @Autowired
    private FlowProcessService flowProcessService;

    @Test
    public void te() {
        LeaveBill o = (LeaveBill) hibernateUtils.getOne("LeaveBill", "id", "333");
        System.out.println("输出： " + o);
    }

    @Test
    public void testException() throws Exception {
        List<String> ids = new ArrayList<>();
        flowProcessService.addForCandidateUser("123", ids);
    }

}
