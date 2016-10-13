# matrix-template

该工程是`Matrix`项目Java组开发微服务业务的模板工程，后续业务开发时请使用该工程作为基础模板。该工程包含丰富的基于Dubbo框架提供REST服务提供测试类，以及如何使用Dubbo框架提供远程服务测试类，使用时请修改相应配置为所属业务配置，并做相应的裁剪。

工程提供模板工程如下：
* 基本接口工程 -> matrix-facade-template  [关于介绍](CHANGELOG.md)
* 基本服务工程 -> matrix-service-template  [关于介绍](CHANGELOG.md)

## 工程介绍

1. 基本接口工程`matrix-facade-template`介绍
    * 提供接口工程中常用依赖，包括`Java REST API——JAX-RS 2.0`数据校验，及`Hibernate Validate`扩展数据校验；
    * 提供JBoss [RestEasy](http://resteasy.jboss.org/)框架功能。

2. 基本服务工程`matrix-service-template`介绍
    * 提供Dubbox服务，支持REST风格远程调用（HTTP + JSON/XML)，支持基于Kryo和FST的Java高效序列化实现，支持基于Jackson的JSON序列化，支持基于嵌入式Tomcat的HTTP remoting体系；
    * 提供MySQL数据库连接；提供c3p0、Druid两种数据库连接池及Druid数据库连接监控UI。
    * 使用JBoss [RestEasy](http://resteasy.jboss.org/)框架，提供REST数据校验功能；
    * 提供`Disconf`动态配置功能；

## 使用注意事项
* 建议以后所有业务代码使用该工程，由该工程统一维护业务工程基础依赖，对于特殊依赖，由具体业务完成；
* 注意查看[changelog](CHANGELOG.md)，浏览`matrix-template`工程每个版本变化。

## 版本更新说明

请查看[changelog](CHANGELOG.md)
