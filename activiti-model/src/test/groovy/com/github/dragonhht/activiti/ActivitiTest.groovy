package com.github.dragonhht.activiti

import com.github.dragonhht.activiti.commands.FreeJumpCommand
import org.activiti.engine.ManagementService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.impl.RuntimeServiceImpl
import org.activiti.engine.task.Task
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-1
 */
@RunWith(SpringRunner)
@SpringBootTest
class ActivitiTest {

    @Autowired
    private TaskService taskService
    @Autowired
    private ManagementService managementService
    @Autowired
    private RuntimeService runtimeService

    @Test
    void testSqlQuery() {
        long count = taskService.createNativeTaskQuery()
                .sql("select count(*) from ${managementService.getTableName(Task.class)} t where t.name_ = #{name}")
                .parameter("name", "bill")
                .count()
        println count
    }

    @Test
    void testJump() {
        ((RuntimeServiceImpl)runtimeService).getCommandExecutor()
            .execute(new FreeJumpCommand())
    }

}
