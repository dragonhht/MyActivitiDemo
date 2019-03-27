package activiti;

import hht.dragon.MyActivitiDemoApplication;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-3-27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyActivitiDemoApplication.class)
public class BaseTest {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    /**
     * 基本流程.
     */
    @Test
    public void testAll() {
        // bpm文件路径
        String bpm = "processes/study/leav-bill.bpmn";
        String key = "leave-bill";
        // 部署流程
        Deployment deployment = repositoryService.createDeployment()
                                    .name("leave-bill")
                                    .addClasspathResource(bpm)
                                    .deploy();
        String deploymentId = deployment.getId();
        System.out.println(deploymentId);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(key)
                .deploymentId(deploymentId)
                .singleResult();


        assert "leave-bill".equals(processDefinition.getKey());

        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
        assert processDefinition != null;

        // 执行任务
        Task task = taskService.createTaskQuery()
                .taskAssignee("deptLeader")
                .deploymentId(deploymentId)
                .singleResult();
        assert task != null;
        assert "领导审批".equals(task.getName());

        // 签收
        //taskService.claim(task.getId(), "leaderUser");
        // 完成任务
        taskService.complete(task.getId());

        task = taskService.createTaskQuery()
                .taskAssignee("deptLeader")
                .deploymentId(deploymentId)
                .singleResult();
        assert task != null;

    }

}
