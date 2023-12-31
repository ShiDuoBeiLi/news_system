## 项目介绍

系统主要分为前台浏览页面和后台管理页面，一些设计介绍如下：

- 系统具有分级功能，即上级部门的用户能够查看下级的信息，但下级的用户只能管理本级的信息或查看更下级的信息，用户在等级上存在隶属关系。例：系统中存在3个等级用户（从低到高依次为lv1、lv2、lv3），在未登录或者登录等级1的账号看到的内容都是在数据库中标为等级1的新闻，等级2就可以看到同级与等级1新闻，等级3依此类推。

前台的功能介绍：

- 系统中所有用户的密码都使用借助了MD5算法来进行加密，使得密码在数据库中不是明文显示，增加安全性（MD5并不是加密算法，但我们可以它的单向特性加密，只能说聊胜于无）

- 前台页面提供了用户注册/登录、新闻浏览、新闻搜索和发布、进入后台的接口。

- 用户在新闻发布时会有相应的敏感词过滤，与词库中的词匹配到用 “*”替代，并且其新闻内容需要经过后台管理员审核通过才能给其他用户观看。这里的敏感词过滤算法为DFA算法，项目中可选最大或最小匹配规则，具体情况可**参考项目路径“main/java/cn/wxm/news_demo/utils”下名为SensitiveWordinit.java和SensitiveWordUtil.java文件**，存放的词库路径为**"resources/static/word.txt"**。该词库暂时存放三千多个敏感词，后期需要添加或修改词库可按一个敏感词一行的格式修改。

- 前台页面具有评论与收藏功能，都需要登录激活成功的账号才可进行，评论也是具有敏感词过滤功能，可在个人中心查看评论或收藏。

后台管理页面则提供新闻分类、新闻审核、新闻评论、用户与新闻的操作管理管理等，需要等级2或等级3的管理员用户才可登录（同时，后台加了拦截器拦截未经登录的用户），可通过数据库t_user表的ad_role字段查找管理员才可登录。后台功能这里就不一一介绍，感兴趣小伙伴可下载后跑一遍即可明白。

## 技术介绍

- SpringBoot
- Mybatis-Plus（MP）
- Thymeleaf——模板引擎
- Layui（前端框架）

系统中用到的模板都与Layui相关，后台管理模板：[X-admin: X-admin经典前端后台管理模板 (gitee.com)](https://gitee.com/daniuit/X-admin)

## 案例展示

首页展示：

![首页展示](https://raw.githubusercontent.com/ShiDuoBeiLi/news_system/master/newsImg/index.png)

新闻详情页：

![新闻详情页](https://raw.githubusercontent.com/ShiDuoBeiLi/news_system/master/newsImg/detail.png)

用户中心：展示收藏和评论的新闻

![用户中心](https://raw.githubusercontent.com/ShiDuoBeiLi/news_system/master/newsImg/center.png)

新闻发布页：

![新闻发布页](https://raw.githubusercontent.com/ShiDuoBeiLi/news_system/master/newsImg/public.png)

后台首页：

![后台首页](https://raw.githubusercontent.com/ShiDuoBeiLi/news_system/master/newsImg/admin.png)

论文目录：

![论文目录](https://raw.githubusercontent.com/ShiDuoBeiLi/news_system/master/newsImg/593febb06dca2a61a4b015f1c6fbac9.png)

## 一些说明

- 所有**登录用户密码均为“admin1”**,管理员需要结合用户表的ad_role和lid字段来判断
- 系统和论文都可便宜出售（v:zhou135431）




