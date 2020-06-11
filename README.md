
## 通知

- 基于 Sculptor Boot 体系的 CMS 系统正在开发中，采用 PostgreSQL 进行存储、全文检索，适合中小企业
- 目前只提交到私有的 Gitlab 中，有需要跟进的请下拉读到授权部分说明
- Github、Gitee 的版本计划在 3 个月后发布
- 视频教程已录制：<https://space.bilibili.com/15713069/channel/detail?cid=115644>

## 只有上云才能撑住规模化后的发展

- 初期技术选型上尽可能寻找云支持的
- 在公司规模小，自建服务基本都做不到 99.999% 高可用
- 在公司规模发展变迅速时，如果云技术和已有技术契合，迁移成本会低很多很多
- 目前暂定只选择：[阿里云服务](https://www.aliyun.com/minisite/goods?userCode=v2zozyxz)
- 这里罗列了阿里云常用的一些：[产品](https://github.com/cdk8s/cdk8s-team-style/blob/master/ops/aliyun.md)
- 关于大数据方案阿里云直接提供了教程，所以我觉得我没必要继续说了：[大数据路线](https://edu.aliyun.com/roadmap/bigdata)

## Sculptor Boot：项目思维化的《代码生成器》体系（未来可期，做个朋友吧）

- 部分观点过于激进，未成年请在成年人的陪同下阅读：`Data is everything, everything is data`
- **Sculptor Boot** 体系包含：
    - 数据库脚本逻辑：MySQL、PostgreSQL（下个版本采用）
    - 代码生成器：Java
    - 后端项目：Spring Boot、Spring Cloud Alibaba（分布式版本）
    - 前端项目：React、Vue（下个版本采用）
- **是的，它们是不能解决所有问题。但是，可以解决部分问题**

-------------------------------------------------------------------

## Git

- `TKey`：[Github](https://github.com/cdk8s/tkey)、[Gitee](https://gitee.com/cdk8s/tkey)
- `sculptor-boot-generator`：[Github](https://github.com/cdk8s/sculptor-boot-generator)、[Gitee](https://gitee.com/cdk8s/sculptor-boot-generator)
- `sculptor-boot-backend`：[Github](https://github.com/cdk8s/sculptor-boot-backend)、[Gitee](https://gitee.com/cdk8s/sculptor-boot-backend)
- `sculptor-boot-frontend`：[Github](https://github.com/cdk8s/sculptor-boot-frontend)、[Gitee](https://gitee.com/cdk8s/sculptor-boot-frontend)
- `sculptor-boot-test`：[Github](https://github.com/cdk8s/sculptor-boot-test)、[Gitee](https://gitee.com/cdk8s/sculptor-boot-test)
- `sculptor-boot-docs`：[Github](https://github.com/cdk8s/sculptor-boot-docs)、[Gitee](https://gitee.com/cdk8s/sculptor-boot-docs)


-------------------------------------------------------------------

## Live Demo

- **注意：带宽只有 1M，访问会慢** 
- **本地添加下面 hosts 才能正常访问和重定向到登录页面（必备）**

```
182.61.44.40 sculptor.cdk8s.com
```

- 访问地址：[CDK8S Sculptor Boot](http://sculptor.cdk8s.com/sculptor-boot-frontend/)


-------------------------------------------------------------------

## Gif 效果演示（单表的前后端 CURD 生成）

- 说明：为了减小 Gif 体积进行了加速
- [单表生成代码过程](http://img.gitnavi.com/sculptor-boot/sculptor-boot-gen.gif)
- [单元测试过程](http://img.gitnavi.com/sculptor-boot/sculptor-boot-junit.gif)

#### 演示步骤

- 初始化数据库脚本
- 执行生成操作
- 添加生成文件到版本控制
- 插入权限 SQL 到数据库
- 执行单元测试：

```
mvn clean test -Dtest=SysDemoMapperTest,SysDemoServiceTest,SysDemoControllerTest
```

- 启动后端应用，执行接口压力测试：

```
mvn gatling:test -Dgatling.simulationClass=test.load.cdk8s.SysDemoPage
```

- 启动前端应用，演示 CURD 操作

-------------------------------------------------------------------

## bilibili 完整视频演示

- [Sculptor-Boot-代码生成器-CDK8S](https://www.bilibili.com/video/av79864146/)

#### 部署、性能相关 Gif 图

- 因为和 TKey 一样的架构，有部分图直接拿过来用了，这里不累赘了
- 服务器的部署教程、配置文件大家可以直接到 TKey 项目上拿：`TKey`：[Github](https://github.com/cdk8s/tkey)、[Gitee](https://gitee.com/cdk8s/tkey)

![JProfiler 压测变化](http://img.gitnavi.com/tkey/tkey-jprofiler.gif)

- **登录完整过程：** [主图](https://upload-images.jianshu.io/upload_images/19119711-cd483cefb50eb763.gif)、[备图](http://img.gitnavi.com/tkey/tkey-sso-login.gif)
- **Grafana 监控大屏：** [主图](https://upload-images.jianshu.io/upload_images/19119711-af9b3d3411db1da1.gif)、[备图](http://img.gitnavi.com/tkey/actuator-prometheus-grafana.gif)
- **GoAccess 监控大屏：** [主图](https://upload-images.jianshu.io/upload_images/19119711-b3bcc4edcf0df007.gif)、[备图](http://img.gitnavi.com/tkey/goaccess-data.gif)
- **Postman 接口调用：** [主图](https://upload-images.jianshu.io/upload_images/19119711-a8316b794bf4bf56.gif)、[备图](http://img.gitnavi.com/tkey/postman-request-api.gif)
- **Docker 容器管理：** [主图](https://upload-images.jianshu.io/upload_images/19119711-281dd6b40f2d7fc7.gif)、[备图](http://img.gitnavi.com/tkey/portainer-docker.gif)
- **Jenkins 部署流水线：** [主图](https://upload-images.jianshu.io/upload_images/19119711-2d20e2fba98ddbbd.gif)、[备图](http://img.gitnavi.com/tkey/tkey-jenkins.gif)
- **JProfiler 压测变化：** [主图](https://upload-images.jianshu.io/upload_images/19119711-922b8202de206b06.gif)、[备图](http://img.gitnavi.com/tkey/tkey-jprofiler.gif)
- **VisualVM 压测变化：** [主图](https://upload-images.jianshu.io/upload_images/19119711-067bcdf1a6e95b44.gif)、[备图](http://img.gitnavi.com/tkey/tkey-visualvm.gif)


#### 功能模块截图

![后台系统表结构模型 Navicat 生成](http://img.gitnavi.com/sculptor-boot/sculptor-boot-database-model.png)
![功能介绍](http://img.gitnavi.com/sculptor-boot/sculptor-boot-ui-1.png)
![功能介绍](http://img.gitnavi.com/sculptor-boot/sculptor-boot-ui-2.png)
![功能介绍](http://img.gitnavi.com/sculptor-boot/sculptor-boot-ui-3.png)
![功能介绍](http://img.gitnavi.com/sculptor-boot/sculptor-boot-ui-4.png)
![功能介绍](http://img.gitnavi.com/sculptor-boot/sculptor-boot-ui-5.png)
![功能介绍](http://img.gitnavi.com/sculptor-boot/sculptor-boot-ui-6.png)
![功能介绍](http://img.gitnavi.com/sculptor-boot/sculptor-boot-ui-7.png)
![功能介绍](http://img.gitnavi.com/sculptor-boot/sculptor-boot-ui-8.png)

-------------------------------------------------------------------

## 技术栈

- 暂时只能生成 `Spring Boot 2.1.11` 相关代码，下阶段是生成 `Spring Cloud Alibaba`
- 2020 年 1 月中旬会在圈子内部发布基于该生成体系的 CMS 系统（PC + H5 + 小程序）来验证业务灵活度
- 没有任何封装，没有任何加密、混淆，一眼可以看出项目结构。接下来还有大量细节要继续优化，可以持续关注下。
- 有轻微强迫症：
    - 启动不能有 error、warn
    - 代码格式化得整整齐齐，该是 Tabs 绝不会是 Spaces，该省去的注释一个都不会加

## 代码生成器理念（项目思维化的解释）

- 群里有人说：不能生成复杂的功能代码，有啥屁用。我不反对这样的说法，同时猜测认定他应该不是领导者。
    - 能减少下属开发人员抽身出重复的工作，不说效率提高如何如何，就心情愉悦程度就不一样。没人愿意一直做一些重复事情。重复事情做多了容易造成下属思考频率降低，没意思，留不住人。
    - 会是复杂的功能基本都是核心流程，或者核心的分支。没有哪个项目经理、产品经理动不动就调整复杂流程，做个全回归测试就可以把所有人搞死。
    - 即便是复杂流程，也是由很多细小的简单流程构建。如果不是，那就是在做系统分析的时候就是出了问题，因为这种说不清楚逻辑的系统后期基本也无法维护，也没人愿意接手这样的工程项目。
    - 有很多时候，所谓的复杂流程，其实可以通过交互设计得更加简单，可能操作上确实会多了几步路，但是开发复杂度可能就降低几个指数。这里就很考量产品人员与开发人员的配合，当然，不是所有的简化都叫做简化，有的叫做懒，别给自己找借口。
    - 当团队技术不行的时候，有时候是可以通过管理来凑，但是本质还是要不断提高技术能力，这个不需要解释。
- 目前业界的代码生成方式有：
    - `类 CUBA Studio 高度定制 IDE` 
    - `类 Swagger 的 OpenAPI`
    - `类 JHipster cli`
    - `MyBatis Generator`
    - `IDE 插件`
    - `JavaFX 独立软件`
    - `WEB UI 录入`
    - `Java -jar`
    - `Shell 命令`
- 本质都是模板，只是大家对模板的定义各不相同，以及大家各自业务中各种各样的定制需求点。未来必然还会有 AI 的代表，但是短时间还不会成熟方案。
- 以上方式我都不赞同，只因为一个逻辑观点：`不利于全员推广下的 *共同维护与发展*`
- 没有什么事是不变的，没有什么人是完美的。每个人都有自己擅长和不擅长的边界线，我相信总是有人能发现更加美好的道路。所以，作为领导者要尽可能铺好信息反馈与协作的路。
- 编程不只是编程，它最核心的是 `软件工程`，虽然有的工程一个人也可以干，但是更多时候我们需要更多人，需要大家一起不断维护，而能被共同维护的，只能是项目化思维
- 所以，以我目前能力，推荐的方式是：Git 仓库化下的 Application run()。这样有助于培养每个人 Pull Request 意识，共同维护出节省彼此时间的工具
- MyBatis Generator 需要也是这个玩法，但是能生成的代码实在太过于简陋，不符合实际团队的代码生成
- Swagger 的 OpenAPI 虽然 Clone 下来也可以改造模板，但是它更擅长 API 类的异构平台代码生成，改造成整套业务模块的成本太大。
- 最高效的方案应该是：类 CUBA Studio 高度定制自己团队 IDE，但是国内很少有团队有这个实力。但是，我相信未来 VSC 阵营的童鞋应该有人会出。
- 单元测试真的很重要，不是覆盖率的问题，我做单元测试的思路不是看覆盖率，我只关心业务的核心流程是否能否快速做回归。这对于经常重构代码的人来讲，回归测试真的非常重要。
- 希望 Sculptor Boot 体系能带来新的体验，以及推进国内普及单元测试

-------------------------------------------------------------------

## Sculptor Boot 体系理念

- 项目命名介绍：
    - `sculptor` 雕刻家。\[ˈskʌlptər\]
    - `boot`：采用 Spring Boot 架构
- 目前，市场上很多开发者的代码生成器都是用来辅助后台管理系统新功能的开发的。但是，我是为了写这套代码生成器，顺便开发了一套前后端分离的后台管理系统来证明它可用
- **Sculptor Boot** 体系下所有项目代码 90% 都是自动生成的。在我强迫症要求下，在多次更改模板之后，不断重新生成所有模块代码，重新做单元测试，反复对比修改过的地方，我不知道自己为此花了多少时间来苛刻自己
- **Sculptor Boot** 想要做的是：模板一切，但又要保持任何场景的灵活性
- **Sculptor Boot** 坚持查询必须用 XML SQL，不应该用 Java 代码来代表 SQL 的逻辑。我知道很多人喜欢用 Mybatis-Plus 来扩展 MyBatis 为全自动 ORM，站在灵活的角度上，我对此表示反对
- **Sculptor Boot** 要求绝大多数的业务都必须有逻辑删除、状态控制，不允许浪费数据。
- **Sculptor Boot** 认定单元测试是有无上尊贵的地位，特别是项目的核心模块、核心流程
- **Sculptor Boot** 开发过程中，深刻体会：`不以规矩，不能成方圆`，如果你在团队中没有先起草一个完全可行的开发规范，你很难做到团队代码风格一致。如果还能跟我一样，会利用 IntelliJ IDEA 的相关特性，那可以使得规范更进一步。
- **Sculptor Boot** 这套体系的完整的来源链路是：`IntelliJ IDEA > 编码规范 > 代码生成器 > 前后端分离的后台管理系统`
- 你想要如视频中演示的那样快速开发一个功能，那你就必须认同 IntelliJ IDEA 逻辑，认同这套编码规范，不然这套体系的技术栈都是常规的，没啥太大新鲜度，对你来讲可能没啥价值，纯属浪费时间
- 如果你要重构整套体系，本质跟你重写的工作量是没啥区别，我反而是建议你参考它的你认为的可取逻辑点，自己也模仿造一套，这样会更加顺手。
- 这套体系只能适用于新系统，老系统基本无法过度过来，只能当做参考进行学习
- 最后：我坚信，也因为它在编码规范上的强约定，必须性的遵守，所以它有更大的可能实现各种重复性工作的抽象


-------------------------------------------------------------------


## 架构

- 因为就是一个 Spring Boot 结构，用脚想也知道有哪些东西，不画架构图了，有兴趣的直接看 pom.xml
- 登录模块基于我的另外一个作品：TKey，所以严格上这套体系也算是 TKey 的另外一个资料，同时也是未来 `Spring Cloud Alibaba` 架构下 `UMPS` 的一部分
    - TKey 项目是在最精简的 Spring Boot 环境下进行开发，项目文档详细地说明了关于如何做 `规范、开发、压测、部署、监控` 等细节，优先推荐先阅读一下。
    - TKey 采用 OAuth 协议，所以登录环节你会看起来很复杂，绕来绕去的，简单的一个数据库的验证用户名密码走了老大一圈。不想用密码模式，采用了授权码模式必然就是这样的。
    - 当然，也因为支持了 TKey 的 OAuth，所以扩展性上就很好了，相当于直接把这个系统变成了开放平台了，可以让别人快速接入。所以，如果要研究登录过程，不推荐你直接基于此项目进行研究，而是看 TKey 项目，那个是最简洁的情况，研究起来不会有各种干扰。
    - 因为用了 TKey 所以就不需要 `Shiro` 和 `Spring Security` 了，在登录认证的各个细节你就可以各种定制，爱怎么处理就怎么处理，老板各种非工程化的需求你都可以实现。
- 关于使用 `JPA` 还是 `MyBatis`，我这里是倾向于 MyBatis，并且是有 XML 的 MyBatis，不推荐注解和类代码块，原因如下：
    - 国内大部分中小团队在建模能力上不够，并且这类团队的老板一般也不够工程化思维，在这种复杂条件下，灵活的 SQL 书写会更加好维护
    - XML 的对称式结构更加易于代码模板生成
    - IntelliJ IDEA 对 XML 支持很好，如果再配置上 Database 之后，还有各种字段提示、拼写检查、锚点点击定位、跳转等等
    - XML 更加便于阅读、修改、复制、格式化
    - 通过一些 IntelliJ IDEA 的 MyBatis 商业插件可以在书写 XML 上再次提高效率
    - 对于 XML 还是 Class，我觉得是个人审美观点差异，没有对错，都是可以用的，自己觉得方便就行。但是如果用这套生态体系那就只能是 XML 了
- 如果后面 `Spring Data R2DBC` 成熟了，只能用 JPA 才能响应式，那到时候我再切换 ORM 或者书写方式。我是认定响应式的未来是无法阻挡，只是这时候尚早。


-------------------------------------------------------------------


## WEB 新人开发者视频教程（版权归原作者所有）

- 如果不会 Spring Boot 建议先学习下面基础资料再进行使用。学习是有方法技巧的，有对应基础学起来才会有实效。
- [项目管理的逻辑（清华大学）](https://www.bilibili.com/video/av58450290)
    - 先不说你们团队能不能做，但是你必须知道这是各种前辈死里求生地总结，你必须知道你写代码到底要干啥，为谁服务。公司不行，团队不行，那就是另外一个问题，自己要的心里有底。
    - 再次强调：这不是项目经理专属的工作，而是整个软件工程中任意一员都需要懂得道理，除非你翅膀硬了。
    - 项目管理本质我认为是时间。时间可以成本，可以质量，可以市场。可惜的是：每个人都只有 24 小时，都不是大风刮来的。
    - 大部分一般性的 WEB 开发团队都不是死在技术上，基本都是死于管理上。目前的 WEB 开发还有啥特别艰难研究的技术？不接受反驳。
- [Git & Github 教程](https://www.bilibili.com/video/av24441039)
    - Git 你必须不择手段地学会，你可以不用记那么多命令，但是理念必须。大部分操作 IntelliJ IDEA 都有 UI 按钮，所以一定要学习协作开发的理念。
- [WEB 协议和抓包](https://www.bilibili.com/video/av77946469)
    - 这是做 WEB 的基础中的基础，没什么好说的，记不住所有知识点没关系，但是至少得做笔记，有个印象，方便以后查
- [IntelliJ IDEA神器使用技巧](https://www.imooc.com/learn/924)
    - 我自己很早那套视频教程好像还是 2012 年，并且那时候我才刚学习，所以已经不推荐了，这套视频挺好的，就是内容少了点
- [IntelliJ IDEA 简体中文专题教程](https://github.com/judasn/IntelliJ-IDEA-Tutorial)
    - 这套是我写的，内容多，同时适用于 JetBrains 家常见的 IDE
    - 未来如果我们学习数据分析、人工智能必然也会用到 PyCharm，所以 IntelliJ IDEA 你必须学，不然我们做不了朋友
- [MySQL 必修课](https://www.bilibili.com/video/av77689286?p=46)
    - 建议主修查询、分析 SQL 逻辑。视图，存储过程了解即可。
- [Spring Boot 基础教程-1](https://www.bilibili.com/video/av38657363)
- [Spring Boot 基础教程-2](https://www.bilibili.com/video/av62047875)
- [Spring Boot 扩展教程](https://www.bilibili.com/video/av45734793/?p=1)
    - 这一套扩展了：Linux、Docker。这两个技能树你也必须点亮，没有任何理由和借口
- [软件项目团队沟通桥梁之UML（非必须，但是建议）](https://www.bilibili.com/video/av75100997)
- [CDK8S 团队规范（非必须，可以借鉴））](https://github.com/cdk8s/cdk8s-team-style)


-------------------------------------------------------------------

## 文档

- 为了统一管理，也方便他人查阅，文档独立出一个项目
- `sculptor-boot-docs`：[Github](https://github.com/cdk8s/sculptor-boot-docs)、[Gitee](https://gitee.com/cdk8s/sculptor-boot-docs)


-------------------------------------------------------------------

## 授权协议

- **Sculptor Boot 体系和 TKey 都已递交软著申请。Sculptor Boot 体系源码是 GPL 协议开源，要商业闭源使用的前提：我们必须是朋友。**
- 做朋友跟商业授权不一样的，商业授权是客观商业行为，做朋友是主观个人行为，表示你认可我的做人，做事理念，不认可作者理念的没有资格获得使用权。
- 朋友嘛也不设置什么阅读权限、源码权限，只希望认可相关理念的可以一起玩，看好社会科学、数据分析、人工智能的希望可以一起学习，为接下来的 5G、AI 时代做准备。

## 做朋友的方法

- 要认同我理念，然后赞赏我
- 可以到我的微信公众号，随便找个有赞赏的文章，拉到最底下，赞赏价：256

## 作者做人做事理念

- 你不应该浪费在讨好他人的路上。你明天就是死在外边，也没人会在意，别不信。
- 要不断思考自己与世界的意义，不断地学习、认知
- 有 Power 和没 Power 的核心是：选择优先。认清自己现状，尽自己最大能力，做好每次选择。
- 不要想着一个人单干。一个人的力量在现今完全不够塞牙缝中的牙缝。
- 做一件事达到 60 分可能比较简单，但是做到 80，90 分那也许就是与全世界为敌。所以，我们做领导的要有自知之明，你选的对手决定你付出的代价，别只会空话瞎比比。

## 作者 WEB 与 AI 理念

- 本系列系统不会优先想着如何优化包、减小体积、提高启动速度、响应速度。我优先的逻辑是：清晰、易协作、好维护、可扩展
- **WEB 已死，也许夸张了点，但是会越来越不值钱，特别是普通 CURD 类型的开发者。当然，如果你连整套 WEB 体系都还没建立，那我只能安慰你节哀顺变。**
- 现阶段，我打算放弃看到 Service Mesh 普及，放弃等到 Micronaut、Quarkus、Helidon、Payara、Thorntail、Javalin 框架全雄争霸那天，只想简简单单用臃肿的 Spring 生态来收集数据，学会利用数据
- 普通 WEB 行业技术越来越成熟，门槛越来越低，我不想再跟任何人争辩这个话题。当然，高标准的事、高端的人才永远都是有，任何时代都是，只是你有没有这个命而已
- 我开发这套体系不是为了恰谁的饭，而是我想让更多同行和我一样空出时间，大家一起学习用户研究、数据获取、数据分析、人工智能。
- WEB 只是数据的入口之一，是会存在，但是不应该再花费我们太多精力，我们应该更加全面地了解数据化的世界。
- 你自己回头看下过去 4G 的普及用了多少年，以及全球对 5G 的普及打算用多少年。可你准备好了吗？
- 我们为什么不可以联合起来学习？共享材料、共享数据、共享方法，共享哪怕是一丁点的合作机会？
- 大部分人也只是想养家糊口，赚点小钱，不用整天搞得四面都是敌人。你的敌人永远比你强大数百倍，数千倍，在你身边的只能是朋友，因为你不够资格做那些人的朋友。你不够格。
- 未来高效的科幻世界既然到来，学习用户研究、数据获取、数据分析、人工智能是真的有意思。人工智能的好，在于可以更好地量化每个人，不管是量化自己，量化他人，还是量化社会。数学能力不行又怎么样？干就是了。
- 对于喜欢科幻片的人来讲是这是一个机遇。能不能加速殖民外太空就看未来 AI 的发展。
- **我喜欢科幻，喜欢未来世界，顺路做个 Amazon 的广告：[《苍穹浩瀚 第四季 The Expanse Season 4》](https://movie.douban.com/subject/30234319/)已于近期播出，有兴趣的童鞋也可以关注**

## 现状

- 2020-06
- 目前感谢积木网老乔的认可我的理念，让我可以去组建团队、打造产品，实现自己的理念，在此深深地表示感谢!
- 本人的开发理论是一致倾向于简单、可维护、易扩展，不细扣性能，只要能横向扩展，方便毕业生上手就是我的目标，感谢认可此目标的同道。

-------------------------------------------------------------------

## 统一联系方式

![CDK8S 官网](http://img.gitnavi.com/markdown/cdk8s-official-website.png)

- 正式启动组织域名：<http://cdk8s.com>
- CD 是 `持续交付` 简写（Continuous Delivery）
- K8S 是 `Kubernetes` 简写
- 可以在上面找到所有项目的联系方式
    - [CDK8S](http://cdk8s.com)
    - [Github](https://github.com/cdk8s)
    - [Gitee](https://gitee.com/cdk8s)
    - [bilibili](https://space.bilibili.com/15713069)
    - [邮箱：cdk8s#qq.com](http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=1baxvu2mlaSk_7a6uA)
    - [圈主微信：judasn](http://img.gitnavi.com/markdown/judasn-weixin-qr.jpeg)
    - [微信公众号：cd-k8s](http://img.gitnavi.com/markdown/cdk8s_qr_300px.png)
- 不得不面对世界的硬：我将不再推荐使用博客，不再发表零散文章。目前业界博客的评论通知生态已经全部阵亡，已经不再适合这个时代。
- 以后要发表内容必然尽可能是一整套体系，只维护于 Github、Gitee、Gitlab（私有）仓库中
- 视频统一放在 bilibili，因为微信公众号暂时不给我评论沟通，我只能利用 bilibili 的评论来做活动。当然，小破站在业界不错。
- 我自己过去都不怎么用公众号、XXX号，就是因为它太封闭了，但是从今以后就得随波逐流。不是因为真香，而是我发现国内渠道已经没有其他路可以走了

-------------------------------------------------------------------

## 牵线

- 我亲弟 24 岁（1995），前端开发工程师，这套体系的前端部分由他开发，也在广州，178cm，55kg 上下，偏瘦，也是一表人才。
- 欢迎有相同价值理念的 IT 女性青年可以成为一家人（记得说明来意，有优先权）


## 感谢

![工作台](http://img.gitnavi.com/markdown/meek-workbench.jpg)

- 感谢：家人
- 升降桌
- 显示器
- 键盘
- 鼠标
- IntelliJ IDEA
- macOS
- www


