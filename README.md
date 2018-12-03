# README-KAOS-Service
## 项目构建
### 运行环境
----
MAVEN  
MongoDB  
jdk1.8  
docker

### 导入项目
#### MAVEN配置
----
**file**-**new**-**project from Existing sources**,选择项目所在文件夹，点击Import project from external model-Maven,Environment Settings配置好Maven，点击next-next-finish.(MAVEN配置可参考<a href="https://blog.csdn.net/qq_37599827/article/details/80630848" target="_blank">https://blog.csdn.net/qq_37599827/article/details/80630848</a>)  
#### mongodb配置
##### 本地mongodb
----
创建一个test数据库，在其中添加一条用户名为test1,密码为test1的用户。（默认地址和端口为127.0.0.1:27017）  
##### 在docker中安装mongodb
----
+ `docker pull mongo`  
+ `docker run -p 27017:27017 mongo`
##### 检验mongodb服务是否启动
----
- 在浏览器中访问<a href="http://localhost:27017" target="_blank">http://localhost:27017</a>
- 运行项目文件夹test中service的demoInfoTestService
## 项目运行
----
+ 保证mongodb正常运行
+ 运行DemoApplication.java  
+ 在浏览器中访问<a href="http://localhost:8080/" target="_blank">http://localhost:8080/</a> + 对应的网页字符串
## 项目在docker打包
----
+ 在项目路径下运`mvn build`
+ 将target文件夹中的demo-0.0.1-SNAPSHOT.jar放到docker文件夹下
+ 在docker文件夹里面运行`docker build -t kaos-service .`
## 目录结构
```
/src/main/java/com/test/
​    |-- controller/                     //控制器
​          ImportFromMongo.java              //从数据库中导入
​          Login.java                        //登录
​          Register.java                     //注册
​          SaveToMongodbController.java      //保存进数据库
​          ServletDownloadController.java    //下载文档
​          TemplateFileController.java       //模板文档
​          ......
​    |-- entity/                         //对应数据库数据的实体
​    |-- filter/SessionFilter.java           //Servlet过滤器
​    |-- repository                      //提供访问数据库的接口
​          KaoserFileRepository.java         //Kaos文档库
​          UserRepository.java               //用户库
​    |-- service                         //服务类
​          FindByMynameService.java          //按名字查询文档
​          LoginService.java                 //登录
​          RegisterService.java              //注册
​          SaveToMongodbService.java         //保存进数据库
​          TemplateFileService.java          //模板文档
​    |-- DemoApplication.java            //程序主入口
/src/main/resources/
​     application.properties             //项目所采用的配置，包括设置端口
​     templates/                         //需求文档模板
/src/test/java/com/test/           //测试程序
......
