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


