package hht.dragon.controller;

import hht.dragon.activiti.FlowProcessService;
import hht.dragon.activiti.model.BusinessKeyModel;
import hht.dragon.entity.LeaveBill;
import hht.dragon.utils.HibernateUtils;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description.
 *
 * @author: huang
 * Date: 17-11-2
 */
@RestController
public class IndexController {

    @Autowired
    private FlowProcessService flowProcessService;
    @Autowired
    private HibernateUtils hibernateUtils;

    @RequestMapping("/index")
    public String index(HttpSession session) {
        session.setAttribute("user", "hht");
        flowProcessService.deploy("processes/LeaveBill.bpmn", "processes/LeaveBill.png", "LeaveBill");
        return (String) session.getAttribute("user");
    }

    @RequestMapping("/start")
    public String start(String id) {
        String key = "LeaveBill";
        flowProcessService.startProcess(key, id);
        return key;
    }

    @RequestMapping("/tasks")
    public String tasks(String name) {
        List<Task> tasks = flowProcessService.getPersonTasks(name);
        return tasks.get(0).toString();
    }

    @RequestMapping("/compele")
    public String compele(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("outcome", "批准");
        flowProcessService.compele(id, map);
        return "ok";
    }

    @RequestMapping("/ok")
    public boolean ok(String id) {
        return flowProcessService.isCompeled(id);
    }

    @RequestMapping("/businesskey")
    public BusinessKeyModel businessKey(String taskId) {
        return flowProcessService.getBusinessKey(taskId);
    }

    @RequestMapping("/get")
    public Object get() {
        LeaveBill o = (LeaveBill) hibernateUtils.getOne("LeaveBill", "id", "333");
        return o;
    }


}
