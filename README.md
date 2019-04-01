# 我的Activiti学习

## Spring Boot2整合Activiti 7

-   添加依赖：

```
<dependencyManagement>
<dependencies>
  <dependency>
    <groupId>org.activiti.dependencies</groupId>
    <artifactId>activiti-dependencies</artifactId>
    <version>7.1.10</version>
    <scope>import</scope>
    <type>pom</type>
  </dependency>
</dependencies>
</dependencyManagement>

<dependencies>
<dependency>
  <groupId>org.activiti</groupId>
  <artifactId>activiti-spring-boot-starter</artifactId>
</dependency>
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
</dependency>
</dependencies>

<!-- 仓库信息 -->
<repositories>
<repository>
  <id>alfresco</id>
  <name>Activiti Releases</name>
  <url>https://artifacts.alfresco.com/nexus/content/repositories/activiti-releases/</url>
  <releases>
    <enabled>true</enabled>
  </releases>
</repository>
</repositories>
```

-   基本配置

```yaml
spring:
  activiti:
    # 指定数据库schema
    database-schema: actstudy
    database-schema-update: true
    history-level: full
```

## Activiti的介绍

-   几个重要的引擎Service接口

    1.  `RepositoryService`: 流程仓库Service,用于管理流程仓库，如：部署、删除、读取流程资源
    
    2.  `RuntimeService`: 运行时Service,可以处理所有正在运行状态的流程实例、任务等
    
    3.  `TaskService`: 任务Service,用于管理、查询任务,例如签收、办理和指派等
    
    4. `HistoryService`: 历史Service, 可以查询所有历史数据，例如，流程实例、任务、活动、变量、附件等
    
    5.  `ManagementService`: 引擎管理Service，主要用于查询引擎配置、数据库、作业等，和具体业务无关
    
-   几个重要组件

    1.  `Activiti Engine`: 最核心的模块，提供针对BPMN2.0规范的解析、执行、创建、管理(任务、流程实例)、查询历史记录并根据结果生成报表
    
    2.  `Activiti REST`: 提供restful风格的服务，允许客户端以JSON的方式与引擎的REST API交互
    
## 具体操作

#### 1、部署流程资源的几种方式

-   `流部署zip包资源`

```
/**
 * 部署流程定义.
 * @param file zip文件
 * @param name 流程名
 */
public void deploy(MultipartFile file, String name) throws IOException {
    ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
    repositoryService.createDeployment()
            .name(name)
            .addZipInputStream(zipInputStream)
            .deploy();
}
```

-   `部署classpath下的资源`

```
/**
 * 部署流程定义.
 * @param bpmnName classpath中bpm文件
 * @param pngName classpath中png文件
 * @param name 流程名
 */
public void deploy(String bpmnName, String pngName, String name) {
    repositoryService.createDeployment()
            .name(name)
            .addClasspathResource(bpmnName)
            .addClasspathResource(pngName)
            .deploy();
}
```

-   `以流的形式部署bpm文件`

```
/**
 * 部署流程定义
 * @param input bpm文件流
 * @param name 流程名
 * @param fileName bpm文件名
 * @throws IOException
 */
public void deploy(InputStream input, String name, String fileName) throws IOException {
    Deployment deployment = repositoryService.createDeployment()
            .name(name)
            .addInputStream(fileName, input)
            .deploy();
}
```

-   `通过字符串(流程定义的xml内容)部署`

```
/**
 * 部署流程定义(通过字符串)
 * @param xmlStr xml字符串
 * @param name 流程名
 * @param fileName 文件名
 */
public void deployByStr(String xmlStr, String name, String fileName) {
    repositoryService.createDeployment()
            .name(name)
            .addString(fileName, xmlStr)
            .deploy();
}
```

#### 2、启动流程实例

-   通过流程`key`，不带任何参数：`ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);`

#### 3、获取待办任务

```groovy
def userId = 'deptLeader'
// 获取已签收或直接分配给指定用户的任务
println "当前处理人为 $userId 的任务"
def doingTasks = taskService.createTaskQuery()
        .taskAssignee(userId)
        .list()
for (task in doingTasks) {
    println "name is ${task.name}"
}

println "等待签收的任务"
// 获取等待签收的任务
def waitingTasks = taskService.createTaskQuery()
        .taskCandidateUser(userId)
        .list()
for (task in waitingTasks) {
    println "name is ${task.name}"
}

println "获取所有待办任务"
// 获取所有待办任务, 以上两个方法的综合
def todoTasks = taskService.createTaskQuery()
        .taskCandidateOrAssigned(userId)
        .list()
for (task in todoTasks) {
    println "name is ${task.name}"
}
```

-   任务签收:通过调用`TaskService`下的`claim`方法完成任务的签收

#### 4、完成任务

-   通过调用`TaskService`下的`complete`方法实现任务的完成

## 监听器

-   `org.activiti.engine.delegate.event.ActivitiEventListener`接口: 该接口可实现Activiti的时间监听，该接口有两个方法

    -   `onEvent(ActivitiEvent activitiEvent)`: 该方法用于事件的监听处理，参数`ActivitiEvent`可获取各事件类型及流程节点的相关信息
    
    -   `isFailOnException()`: 该方法在`onEvent`方法抛出异常是调用，如果该方法返回结果为`false`则忽略抛出的异常。若返回`true`，则异常不会被忽略和冒泡，如果事件是API调用（或任何其他事务操作，例如作业执行）的一部分，则事务将被回滚。如果事件监听器中的行为不是关键业务，则建议返回false。
    
    -   `ActivitiEventListener`各事件类型(各类型在枚举类`org.activiti.engine.delegate.event.ActivitiEventType`)列表:
    
    | 事件    |   描述  |
    |--------|--------|
    |ENGINE_CREATED | 监听器监听的流程引擎已经创建完毕，并准备好接受API调用。|
    |ENGINE_CLOSED | 监听器监听的流程引擎已经关闭，不再接受API调用。|
    |ENTITY_CREATED |创建了一个新实体。实体包含在事件中。|
    |ENTITY_INITIALIZED |创建了一个新实体，初始化也完成了。如果这个实体的创建会包含子实体的创建，这个事件会在子实体都创建/初始化完成后被触发，这是与ENTITY_CREATED的区别。|
    |ENTITY_UPDATED |更新了已存在的实体。实体包含在事件中。|
    |ENTITY_DELETED |删除了已存在的实体。实体包含在事件中。|
    |ENTITY_SUSPENDED   |暂停了已存在的实体。实体包含在事件中。会被ProcessDefinitions, ProcessInstances 和 Tasks抛出。|
    |ENTITY_ACTIVATED   |激活了已存在的实体，实体包含在事件中。会被ProcessDefinitions, ProcessInstances 和 Tasks抛出。|
    |JOB_EXECUTION_SUCCESS  |作业已成功执行。该事件包含已执行的作业。|
    |JOB_EXECUTION_FAILURE  |作业的执行失败了。该事件包含已执行的作业和异常。|
    |JOB_RETRIES_DECREMENTED    |因为作业执行失败，导致重试次数减少。作业包含在事件中。|
    |TIMER_FIRED    |触发了定时器。事件包含在事件中。|
    |JOB_CANCELED   |取消了一个作业。事件包含取消的作业。作业可以通过API调用取消，   任务完成后对应的边界定时器也会取消，在新流程定义发布时也会取消。|
    |ACTIVITY_STARTED   |一个节点开始执行|
    |ACTIVITY_COMPLETED |一个节点成功结束|
    |ACTIVITY_SIGNALED  |一个节点收到了一个信号|
    |ACTIVITY_MESSAGE_RECEIVED  |一个节点收到了一个消息。在节点收到消息之前触发。收到后，会触发ACTIVITY_SIGNAL或ACTIVITY_STARTED，这会根据节点的类型（边界事件，事件子流程开始事件）|
    |ACTIVITY_ERROR_RECEIVED    |一个节点收到了一个错误事件。在节点实际处理错误之前触发。   事件的activityId对应着处理错误的节点。 这个事件后续会是ACTIVITY_SIGNALLED或ACTIVITY_COMPLETE， 如果错误发送成功的话。|
    |UNCAUGHT_BPMN_ERROR    |抛出了未捕获的BPMN错误。流程没有提供针对这个错误的处理器。   事件的activityId为空。|
    |ACTIVITY_COMPENSATE    |一个节点将要被补偿。事件包含了将要执行补偿的节点id。|
    |VARIABLE_CREATED   |创建了一个变量。事件包含变量名，变量值和对应的分支或任务（如果存在）。|
    |VARIABLE_UPDATED   |更新了一个变量。事件包含变量名，变量值和对应的分支或任务（如果存在）。|
    |VARIABLE_DELETED   |删除了一个变量。事件包含变量名，变量值和对应的分支或任务（如果存在）。|
    |TASK_ASSIGNED  |任务被分配给了一个人员。事件包含任务。|
    |TASK_CREATED   |创建了新任务。它位于ENTITY_CREATE事件之后。当任务是由流程创建时，     这个事件会在TaskListener执行之前被执行。|
    |TASK_COMPLETED |任务被完成了。它会在ENTITY_DELETE事件之前触发。当任务是流程一部分时，事件会在流程继续运行之前，   后续事件将是ACTIVITY_COMPLETE，对应着完成任务的节点。|
    |TASK_TIMEOUT   |任务已超时，在TIMER_FIRED事件之后，会触发用户任务的超时事件，     当这个任务分配了一个定时器的时候。|
    |PROCESS_COMPLETED  |流程已结束。在最后一个节点的ACTIVITY_COMPLETED事件之后触发。 当流程到达的状态，没有任何后续连线时， 流程就会结束。|
    |MEMBERSHIP_CREATED |用户被添加到一个组里。事件包含了用户和组的id。|
    |MEMBERSHIP_DELETED |用户被从一个组中删除。事件包含了用户和组的id。|
    |MEMBERSHIPS_DELETED    |所有成员被从一个组中删除。在成员删除之前触发这个事件，所以他们都是可以访问的。   因为性能方面的考虑，不会为每个成员触发单独的MEMBERSHIP_DELETED事件。|

    -   SpringBoot配置`ActivitiEventListener`:
    
        -   `定义自定义监听器`:
        
        ```groovy
        @Component
        class ActivitiListent implements ActivitiEventListener {
        
            @Override
            void onEvent(ActivitiEvent activitiEvent) {
                if (ActivitiEventType.TASK_COMPLETED == activitiEvent.type) {
                    println "ActivitiEventListener: ${activitiEvent.type}"
                }
            }
        
            @Override
            boolean isFailOnException() {
        
                return false
            }
        }
        ```
        
        -   `配置`:
        
        ```groovy
        @Configuration
        class ActivitiConfig implements ProcessEngineConfigurationConfigurer {
        
            @Autowired
            private ActivitiListent activitiListent
        
            @Override
            void configure(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
                def listents = new LinkedList()
                listents.add(activitiListent)
                springProcessEngineConfiguration.setEventListeners(listents)
            }
        }
        ```
        
    -   使用`RuntimeService`添加监听: 通过`RuntimeService`中的`addEventListener`和`removeEventListener`方法可添加和移除监听
    
    -   监听还可以在流程定义时添加
