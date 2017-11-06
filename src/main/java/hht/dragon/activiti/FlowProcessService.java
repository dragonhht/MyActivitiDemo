package hht.dragon.activiti;

import hht.dragon.activiti.model.BusinessKeyModel;
import org.activiti.engine.task.Task;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 流程服务接口.
 *
 * @author: huang
 * Date: 17-11-5
 */
public interface FlowProcessService {

    /**
     * 部署流程定义.
     * @param file zip文件
     * @param name 流程名
     */
    void deploy(MultipartFile file, String name) throws IOException;

    void deploy(String bpmnName, String pngName, String name);

    /**
     * 启动流程实例.
     * @param key 流程键
     */
    void startProcess(String key);

    void startProcess(String key, String businessKey);

    void startProcess(String key, String businessKey, Map<String, Object> variable);

    /**
     * 查询个人业务.
     * @param id 用户ID
     * @return 用户的个人业务
     */
    List<Task> getPersonTasks(String id);

    /**
     * 通过任务ID获取businessKey.
     * @param taskId 任务编号
     * @return businessKey
     */
    BusinessKeyModel getBusinessKey(String taskId);

    /**
     * 完成任务.
     * @param taskId 任务ID
     * @param variable 参数
     */
    void compele(String taskId, Map<String, Object> variable);

    /**
     * 是否已经完成流程.
     * @param processInstanceId 流程实例ID
     * @return true为流程已经完成，否则为未完成
     */
    boolean isCompeled(String processInstanceId);

}
