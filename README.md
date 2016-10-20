# terra-rs

该工程是`Matrix`项目Java组开发微服务业务的`redis接入`远程服务工程。

工程提供子模块如下：
* 接口模块 -> terra-facade  [关于介绍](CHANGELOG.md)
* 服务模块 -> terra-service  [关于介绍](CHANGELOG.md)

## 模块介绍

1. 接口工程`terra-facade`介绍
    * 提供接口工程中常用依赖，包括`Java REST API——JAX-RS 2.0`数据校验，及`Hibernate Validate`扩展数据校验；
    * 提供JBoss [RestEasy](http://resteasy.jboss.org/)框架功能。

2. 服务模块`terra-service`介绍
    * 提供Dubbox服务，支持REST风格远程调用（HTTP + JSON/XML)，支持基于Kryo和FST的Java高效序列化实现，支持基于Jackson的JSON序列化，支持基于嵌入式Tomcat的HTTP remoting体系；
    * 提供MySQL数据库连接；提供Druid数据库连接池及Druid数据库连接监控UI。
    * 使用JBoss [RestEasy](http://resteasy.jboss.org/)框架，提供REST数据校验功能；

## 使用注意事项
* 注意查看[changelog](CHANGELOG.md)，浏览`terra-rs`工程每个版本变化。

## 版本更新说明

请查看[changelog](CHANGELOG.md)
