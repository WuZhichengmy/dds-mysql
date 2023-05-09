# 系统配置部署说明

## 简介

*由于原工程缺少介绍说明文件，特此撰写文档方便后续使用开发。*

此系统包括前两个前端子系统：管理系统（dds-admin）、展示系统（dds-web）以及一个总的后端子系统（data-display-server），两个前端系统依赖于同一个后端系统也即依赖于同一个数据库（库名：data_display）。

**作为系统管理者：**

系统特有的职能包括展示系统样式配置，可视化动态配置（分析报告配置）

**作为一般用户：**

系统功能包括：首页浏览，检索，数据精炼，数据导出，结果分析，分析报告等

**权限说明**

系统包含最基本的权限功能，用户名默认为admin，密码为123456

## 部署配置流程

### 下载依赖

前端通过node获取依赖，后端使用maven将所需的依赖install下来，注意软件的版本问题（例如JDK此系统使用的是**1.8**）。

### 创建数据库

*系统默认采用的是MySQL数据库，同样适用于所有liquibase支持的数据库。*

在您采用的数据库上创建空数据库，名称与配置文件保持一致，默认名即data_display。

### 填充数据库
1. 将data-display-server\sql\eladmin.sql导入到数据库。
2. 在data-display-server\dds-db\src\main\resources\application.yml中修改datasource以及liquibase等配置信息，以使系统能够通过liquibase配置文件自动填充数据库。

3. 运行DbServiceApplication模块，等待数据库填充。
4. 出现并包含不限于如下数据库表，证明数据库填充成功。

​		![image-20230410135647104](https://github.com/SatoriDSufJan/data-display-server/blob/main/static/image-20230410135647104.png)



5. 数据库填充完成后，启动DataDisplayServerApplication，这是后端系统的总入口，若要对系统进行进一步配置，详见data-display-server\dds-system\src\main\resources\config下的文件。

### 运行前端

1. 启动dds-admin管理系统，使用开发模式进行试运行，即package.json中的dev选项。
2. 新建项目。
​		![image-20230410140829894](https://github.com/SatoriDSufJan/data-display-server/blob/main/static/image-20230410140829894.png)

3. 新建完成后查看数据库中的项目项。

​		![image-20230410141053835](https://github.com/SatoriDSufJan/data-display-server/blob/main/static/image-20230410141053835.png)

4. 复制项目id覆盖至dds-web\src\settings.js中的PROJECT_ID。

​		![image-20230410141309703](https://github.com/SatoriDSufJan/data-display-server/blob/main/static/image-20230410141309703.png)

***	此系统由于管理系统在设计之初是考虑到同时兼顾和管理多个展示系统，因此对于每个展示系统而言，分配一个独有的project_id以示该展示系统独属于某个展示系统。***

5. 根据页面提示，创建资源并导入资源，其中**表名**对应于后端数据库中的**实际表名**，**实体**对应**实体名**（按照Java规范，首字母最好大写）。

   *听说资源的导入有编码条件的限制，我暂时没遇到过，如果导入失败，则可考虑切换导入文件编码。*

   1. 首先创建属性列表，将表格属性手动导入到列表中。
   2. 其次创建规则集，规则集就是相当于对导入文件做了一步映射，如果没有映射的想法，直接在映射关系项进行等值映射即可。
   3. 导入资源，选择定义好的规则集进行映射。
   4. 观察到后端数据库表格导入进输入数据，证明导入成功，同时观察数据库是否存在以创建资源时输入的**实际表名**为名的表，因为此表即是导入数据的存放处，后续的步骤会基于此表展开。

### 更新后端

1. 在dds-core模块的domain目录下仿写创建“*YourEntity*”实体（其中*YourEntity*即为你在前端输入的**实体名**）。

​		![image-20230410144350179](https://github.com/SatoriDSufJan/data-display-server/blob/main/static/image-20230410144350179.png)

2. 在dds-core模块的mapper目录下仿写创建“*YourTableName*Mapper”接口（其中*YourTableName*即为你在前端输入的**实际表名**），其中泛型项填上刚才创建的**实体**。

​		![image-20230410144122321](https://github.com/SatoriDSufJan/data-display-server/blob/main/static/image-20230410144122321.png)


**(2023年4月11日泛型更新已解决)**

3. ~~改动dds-core模块下的service\SearchService.java，在继承类泛型中改成**实体**。~~

4. ~~同样将Mapper和实体改写到SearchServiceImpl的泛型中（位于dds-core下的service\impl）~~			

5. ~~最后将SearchService注入到ServiceConfig（位于dds-core下的config）中，并把它put到serviceMap中。~~

​	~~*第3-5步令人感到匪夷所思，实际上，这种做法破坏了原系统的拓展性，使得SearchService变得只为Test一张表服务，同时第5步直接从代码层面将Service加入到一个map中，也是一种急功近利的做法。*~~

​	~~*对于原项目而言，applicationContext中不知为何获取不到实现SearchService接口的SpringBean，因此这种做法是一种捷径，对于大多数项目需求而言，一管理系统对应一展示系统的关系即可满足要求（一般来说企业的项目也不希望他的管理系统是和别人共用的）。*~~

​	~~*如果有想法的话，可以对这种现状加以改造，使得系统回归到一对多的设计初衷上。*~~

3. 改写添加用于新建展示系统的SearchService（位于dds-core模块的impl目录），注意泛型类型的一致性要求，即对 “class extends ServiceImpl< A, B > implements SearchService< B >” 语句而言，A要求是B的Spring JPA Mapper，B保持一致。
​		![image-20230411140308583](https://github.com/SatoriDSufJan/data-display-server/blob/main/static/image-20230411140308583.png)

4. 重启后端。
	
### 启动展示

可以启动dds-web查看你自定义配置的结果。它包含简介中提到的功能。

## 尾言

此项目仍旧处于开发阶段，部分功能还未完善。
