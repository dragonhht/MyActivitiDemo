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
        flowProcessService.complete(id, map);
        return "ok";
    }

    @RequestMapping("/ok")
    public boolean ok(String id) {
        return flowProcessService.isCompleted(id);
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

    @RequestMapping("/deploy")
    public String deploy() {
        flowProcessService.deploy("processes/UpdateOrDelTest.bpmn", "UpdateOrDelTest");
        return "ok";
    }

    @RequestMapping("/startupdate")
    public String startUpdate(HttpSession session) {
        session.setAttribute("user", "hht");
        flowProcessService.startProcess("UpdateOrDel");
        return "ok";
    }

    @RequestMapping("/startDel")
    public String startDel() {
        flowProcessService.startProcessAndSubmit("UpdateOrDel", "第三次");
        return "ok";
    }

    @RequestMapping("/complete1")
    public String complete1(String taskId) {
        flowProcessService.complete(taskId);
        return "ok";
    }

    @RequestMapping("/complete")
    public String complete(String taskId, String pass) {
       flowProcessService.complete(taskId, pass);
        return "ok";
    }

    @RequestMapping("/setAssignee")
    public String setAssignee(String taskId, String id, HttpSession session) {
        session.setAttribute("user", "qwe");
        flowProcessService.setAssignee(taskId, id);
        return "ok";
    }

    @RequestMapping("/getValue")
    public String getValue(String taskId, String key) {
        return flowProcessService.getVariable(taskId, key);
    }

}
