import hht.dragon.MyActivitiDemoApplication;
import hht.dragon.utils.GetOneUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description.
 *
 * @author: huang
 * Date: 17-11-5
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyActivitiDemoApplication.class)
public class BaseTest {

    @Test
    public void te() {
        GetOneUtils utils = new GetOneUtils();
        utils.getOne("LeaveBill", "333");
    }
}
