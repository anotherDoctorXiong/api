# docker挂载文件


所有docker volume文件都放在/root/docker/volume目录下,该目录需要开放权限chmod 777 或者 docker run -u root 启动
可以使用scp命令在服务器之间拷贝文件夹
宿主机不安装jdk,所有程序都以容器启动,jdk只作为服务的基础镜像使用

## nexus

```bash
# /nexus-data 是nexus的配置文件夹
# 
docker run -d -u root --name nexus  -v /root/docker/volume/nexus/nexus-data:/nexus-data \
-e INSTALL4J_ADD_VM_PARAMS="-Xms512m -Xmx512m -XX:MaxDirectMemorySize=1g"  -p 8081:8081 sonatype/nexus3

```

## nacos

```bash
# 配置表sql
https://github.com/alibaba/nacos/blob/develop/distribution/conf/nacos-mysql.sql
# 相关配置数据都存在mysql,这里主要是连接mysql和日志配置
docker run -d -u root --name nacos  -v /root/docker/volume/nacos/conf:/home/nacos/conf  -p 8848:8848 nacos/nacos-server

# 单机模式按挂载文件启动无法连接mysql必须手动指定
docker run -d -u root --name nacos  -e SPRING_DATASOURCE_PLATFORM=mysql \
-e  MYSQL_SERVICE_HOST=8.135.117.119 \
-e MYSQL_SERVICE_PORT=3306 \
-e MYSQL_SERVICE_DB_NAME=nacos_config \
-e MYSQL_SERVICE_USER=root \
-e MYSQL_SERVICE_PASSWORD=Root_123456 \
-e JVM_XMS=256m -e JVM_XMX=512m -p 8848:8848 -e MODE=standalone nacos/nacos-server:latest 

```

## jenkins 

```bash
# 到容器里面配置下git的ssh 
docker和docker拉去镜像的账户配置需要挂载,ssh需要挂载
# 主要volume的是jenkins工作目录和docker, maven直接在jenkins里面安装
docker run -d -u root --name jenkins -v /root/docker/volume/jenkins:/var/jenkins_home \
-v /usr/bin/docker:/usr/bin/docker \
-v /var/run/docker.sock:/var/run/docker.sock \
-v /root/.docker/config.json:/root/.docker/config.json \
-v /root/.ssh:/root/.ssh \
-e PHP_TZ=“Asia/Shanghai” \
-p 8080:8080  -e JVM_XMS=256m -e JVM_XMX=512m  jenkins/jenkins


```

Refer to [Documentation](https://panjiachen.github.io/vue-element-admin-site/guide/essentials/deploy.html) for more information

## Demo

![demo](https://github.com/PanJiaChen/PanJiaChen.github.io/blob/master/images/demo.gif)

## Extra

If you want router permission && generate menu by user roles , you can use this branch [permission-control](https://github.com/PanJiaChen/vue-admin-template/tree/permission-control)

For `typescript` version, you can use [vue-typescript-admin-template](https://github.com/Armour/vue-typescript-admin-template) (Credits: [@Armour](https://github.com/Armour))

## Related Project

[vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)

[electron-vue-admin](https://github.com/PanJiaChen/electron-vue-admin)

[vue-typescript-admin-template](https://github.com/Armour/vue-typescript-admin-template)

## Browsers support

Modern browsers and Internet Explorer 10+.

| [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt="IE / Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>IE / Edge | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png" alt="Firefox" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Firefox | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png" alt="Chrome" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Chrome | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png" alt="Safari" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Safari |
| --------- | --------- | --------- | --------- |
| IE10, IE11, Edge| last 2 versions| last 2 versions| last 2 versions

## License

[MIT](https://github.com/PanJiaChen/vue-admin-template/blob/master/LICENSE) license.

Copyright (c) 2017-present PanJiaChen
