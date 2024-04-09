# 前言

***

帮助大家找到学习伙伴的移动端 H5 网站（APP 风格），基于 Spring Boot 后端 + Vue3 前端的 全
栈项目，包括用户登录、更新个人信息、按标签搜索用户、建房组队、推荐相似用户等功能

前端源码已经开源Gebase-front 前台界面，[前端源码项目](https://github.com/MegumiN152/yupao-frontend)


## 技术栈

***

Spring Boot + Mybatis(Plus) + MySQL + Redis + WebSocket + Swagger

## 项目前后端部署

***

## 本地部署

## 前端部署方式

1. 代码仓库[前端源码项目](https://github.com/MegumiN152/yupao-frontend)
2. 初始化项目，下载依赖

```
npm install
```

3. 全局搜索

本地部署将其中的127.0.0.1要和前端的访问地址一致，如果前端访问地址为localhost
请将127.0.0.1设置为localhost，后面地址为部署上线的地址，打包之后会根据
isDev判断是的本地环境还是线上环境而使用不同请求地址。
![](doc/code1.jpg)
![](doc/code2.jpg)

4. 在package.json文件中使用 dev 启动

## 后端部署

1. 本地数据库部署运行sql文件
2. 修改配置文件dev中的相关配置

## 安装redis

1. Redis配置(需要本地部署Redis)

```
  session:
    timeout: 86400
  redis:
    port: 6379
    host: localhost
    database: 1
```


# 服务器部署

1. 使用服务器配置为1核2G
2. 宝塔面板

## 前端部署

1. 需要将plugins文件下的myAxios.ts中服务器地址修改为线上后端地址

```
const myAxios = axios.create({
    baseURL: isDev ? 'http://localhost:8080/api':'http://服务器地址/api'
});
```

2. package.json文件中运行build命令，打包成dist文件夹
3. 宝塔使用PHP项目启动前端项目，并修改nginx配置(否则会出现找不到页面404)

```
 location / {
        try_files $uri $uri/ @router;#需要指向下面的@router否则会出现vue的路由在nginx中刷新出现404
        index  index.html index.htm;
     }
      #对应上面的@router，主要原因是路由的路径资源并不是一个真实的路径，所以无法找到具体的文件
      #因此需要rewrite到index.html中，然后交给路由在处理请求资源
     location @router {
         rewrite ^.*$ /index.html last;
     }
```

端口号 5173

## 后端部署

### 服务器安装redis

1. 使用宝塔面板直接安装redis
2. 配置redis https://blog.csdn.net/SpongeBob_shouse/article/details/128909794
3. 配置完记得重新启动redis

### 后端项目打包

1. Redis配置(需要本地部署Redis)

```
  session:
    timeout: 86400
  redis:
    port: 6379
    host: 服务器地址
    database: 1
```

2. 在RedissonConfig文件中配置redis配置

```
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer().setAddress(redisAddress).setDatabase(3).setPassword(password);
```

3. 数据库地址配置

```
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://服务器/yupi
    username: 
    password: 
```

4. session配置

```
server:
  port: 8080
  servlet:
    context-path: /api
    session:
      cookie:
        domain: 服务器地址 #线下环境 域名的访问范围
```

5. 配置文件中头像上传地址需要重新配置,可自定义设置

```
#线上环境
#upload:
#  path: /www/wwwroot/yupi/uploadAvatarImg/
#  address: http://服务器地址:8080/api
```

6. package打包，将打包好的jar文件上传服务器
7. 使用宝塔的java项目运行该jar文件





## 伙伴汇的前端源码

***

yupi-fronted 后台系统
[前端源码项目](https://github.com/MegumiN152/yupao-frontend)
## 说明

***

>如果对您有帮助，您可以右上角点一个“start”支持一下，👍
>
>如有问题请直接在 Issues 中提，或者您发现问题并有非常好的解决方案，欢迎 PR 👍

# 效果展示

***

[查看在线展示].(http://118.31.105.218/)

## 目标功能

***

+ [x] 登录注册功能
+ [x] 推荐用户功能（心动模式）
+ [x] 队伍功能
+ [x] 创建加入队伍
+ [x] 查看队伍信息
+ [x] 个人信息修改功能
+ [x] 根据标签搜索用户
+ [x] 修改标签
  ~~给开发者打钱的支付功能~~

# 总结

1. 后端项目这块，作为自己从学习他人的代码，编程思想，加上自己的想法，自己构思功能的实现，详细到每一步的实现和可能出现的问题
2. 伙伴汇做了很久才完成，但是项目中还有许多许多的细节问题，和没完成的功能，因为自己时间精力有限，暂时没时间完成，以后有时间会继续完善该项目
3. 在对项目做上线部署时，碰见了许多问题，跨域，接口地址错误，cookie种不上，因为服务器上没有redis，直接部署到服务器上出现问题，自己一步步修改bug，不断的锻炼自己修改bug的能力。
4. 线上部署时，项目部署在Linux服务器上时因为图片访问地址和储存地址的问题，配置文件session的作用域要为服务器地址，才能使前后端部署成功。


# 总结

***

1. 在更新这个项目同时自己学习了Redis技术，并学以致用，虽然Redis中有很多技术没用上，如Feed流对信息做推送，因为自己对这部分技术还是不太了解，不能想出这部分业务该如何实现。  
2. 在新增功能的过程中会发现自己之前写代码时没考虑完善留下的坑，设计的缺陷，导致会不断地修改之前的数据库和一些业务代码。
3. 希望这个项目能帮助我不断学习。 
