# Tyr

## 介绍
提尔（Tyr），是北欧神话中的战争与勇气之神，同时也是契约的保证人，誓言的守护者和荣耀的代表。

## 软件架构
软件架构说明
### security
#### try-security
* 基于Spring Security开发和实现的模块。Spring Security是一个功能强大且高度可定制的，主要负责为Java程序提供声明式的 身份验证和访问控制 的安全框架。
* 主要实现了基于Session登录和基于jwt登录的两种方式。
### OAuth2
#### tyr-oauth2-sas
* 授权服务器，基于Spring Authorization Server(简称 SAS)是 Spring 团队最新开发适配 OAuth 协议的授权服务器项目。
#### tyr-oauth2-resource
* 资源服务器，基于Spring Security OAuth模块开发，主要用于管理和保护用户资源。
### sso(单点登录)
#### tyr-sso-core
* sso核心模块，对服务端和客户端提供基础支持。
#### tyr-sso-server
* sso服务端，认证中心。
#### tyr-sso-client-token
* sso客户端，通常和系统的网关/业务模块集成。当前模块是基于token实现的。
#### tyr-sso-client-web
* sso客户端，当前模块是基于Session实现的。

## 文章
* [Cookie,Session,Token,JWT授权方式对比](https://blog.csdn.net/qq_40722827/article/details/131283190?spm=1001.2014.3001.5501)
* [Spring Security系列之基础概念](https://blog.csdn.net/qq_40722827/article/details/131359115?spm=1001.2014.3001.5501)
* [Spring Security系列之认证（Authentication）架构](https://blog.csdn.net/qq_40722827/article/details/131165799?spm=1001.2014.3001.5501)
* [Spring Security系列之授权（Authorization）架构](https://blog.csdn.net/qq_40722827/article/details/131167900?spm=1001.2014.3001.5501)
* 文章持续补充中...
## 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

## 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

## 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

