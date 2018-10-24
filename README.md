# KAOS-Service将项目导入IDEA
##运行环境
MAVEN  
MongoDB  
jdk1.8  
docker
## 导入项目
### MAVEN配置
**file**-**new**-**project from Existing sources**,选择项目所在文件夹，点击Import project from external model-Maven,Environment Settings配置好Maven，点击next-next-finish.(MAVEN配置可参考<a href="https://blog.csdn.net/qq_37599827/article/details/80630848" target="_blank">https://blog.csdn.net/qq_37599827/article/details/80630848</a>)  
###mongodb配置
####本地mongodb
创建一个test数据库，在其中添加一条用户名为test1,密码为test1的用户。（默认地址和端口为127.0.0.1:27017）  
####将mongodb封装在docker中
1.docker pull mongo  
2.docker run -p 27017:80 mongo
3.docker exec -it mongo mongo admin
####检验mongodb服务是否启动
- 在浏览器中访问<a href="http://localhost:27017" target="_blank">http://localhost:27017</a>
- 运行项目文件夹test中service的demoInfoTestService
##项目运行
1.运行DemoApplication.java  
2.在浏览器中访问<a href="http://localhost:8080/" target="_blank">http://localhost:8080/</a> + 对应的网页字符串
##代码结构说明
- /docker/dockerfile 创建docker镜像的配置文件
- /src/main/java/com/test/controller 对应网页的控制器
- /src/main/java/com/test/entity 对应数据库数据的实体
- /src/main/java/com/test/repository 提供访问数据库实体的接口
- /src/main/java/com/test/service 进一步封装以实现访问数据库
- /src/main/java/com/test/DemoApplication.java 程序主入口
- /src/main/resources/application.properties 项目所采用的配置
- /src/main/resources/templates/XXX.html 对应controller的网页
- /src/test/java/com.test 测试程序
- /pom.xml 项目信息以及引入的各种库

