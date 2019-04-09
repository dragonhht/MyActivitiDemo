package com.github.dragonhht.activiti.params

/**
 * 子任务参数key.
 *
 * @author: huang
 * @Date: 2019-4-8
 */
object SubTaskVariableKeys {
    /**
     * 父任务的流程实例id
     */
    val PARENT_PROCESS_INSTANCE_ID = "parentProcessInstanceId"
    /** 父任务id. */
    val PARENT_TASK_ID = "parentTaskId"
    /** 下级流程id. */
    val SUB_INSTANCES = "subInstances"


    // 会签部分
    /** 会签参与人. */
    val SIGN_PERSONS = "signPersons"
}