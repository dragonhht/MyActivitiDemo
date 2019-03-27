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

-   
