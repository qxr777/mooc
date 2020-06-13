MOOC单体版本说明
=============

### 利用docker-compose启动mysql，并导入初始数据

1. 在命令窗口中通过`cd`指令进入项目文件夹的`/docker`目录中

2. 在命令窗口中输入`docker-compose -f app.yml up -d`，将自动下载mysql和springboot-admin镜像至本地，并启动对应服务的两个容器；mysql容器启动后，将自动导入`sql/mooc.sql`

3. 在IDEA中打开mooc项目文件夹，以Run或Debug模式启动`mooc-class`模块中的`edu.whut.cs.jee.mooc.MoocClassApplication`的`main`方法。

4. 启动成功后，通过swagger地址`http://localhost:8080/swagger-ui.html#/`访问 ；或导出Postman共享文件`https://documenter.getpostman.com/view/2988524/Szt8eVeF?version=latest`，在Postman中进行测试。

5. 在Postman中进行任何测试之前，需要首先调用`学生申请令牌`或`教师/管理员申请令牌`, Postman会将得到的`access_token`设置为环境变量组`MOOC_API_ENV`的当前值。注意按照业务流程，进行测试。