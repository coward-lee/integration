
# BeanFactory 的核心接口和实现类
* ListableBeanFactory
* AutowireCapableBeanFactory
* AbstractBeanFactory
* DefaultListableBeanFactory

# FactoryBean 工厂Bean
 在配置文件配置Bean的时候，配置的bean和返回类型可以不一样，而是通过FactoryBean的getObject方法来返回对应的Bean
 
# Bean的作用域
* scope属性 singleton和prototype
# Bean的声明周期
1. Bean的创建，用构造方法去创建一个bean实例
2. 为bean的属性赋值对其他bean引用（调用set方法）
3.     "把Bean的实例给后置处理器的方法，"  BeanPostProcessor#postProcessBeforeInitialization
4. 调用bean的初始化方法（需要进行配置） 
5.     "把Bean的实例给后置处理器的方法，" BeanPostProcessor#postProcessAfterInitialization
6. bean可以使用了
7. 容器关闭的时候，调用销毁bean的方法，(需要进行配置销毁方法)
* 自己实现一个BeanPostProcessor


# 后置处理器

* BeanPostProcessor bean的后置处理器
* BeanFactoryPostProcessor bean工厂的后置处理器


# AOP 术语
1. 连接点：类里面那些方法可以被增强、这些方法称之为连接点
2. 切入点：实际被增强的方法，成为切入点
3. 通知（增强）：实际增强的逻辑部分称之为通知（增强）      
   前置通知、后置通知、环绕通知、异常通知、最终通知
4. 切面是动作：把通知应用到切入点的过程
## 1. AOP的准备操作
1. Spring是一款一般都是基于AspectJ实现AOP操作 
   1、 什么是AspectJ  
      他是单独的一个AOP框架，一般把AspectJ和Spring框架一起使用、进行AOP操作 
2. AspectJ实现AOP的两种方式 
   1. 基于XML配置文件   
   2. 基于注解的方式实现
3. 引入相关jar包 spring-aop， spring-aspect, spring-expression
4. 切入点表达式 
   1. 切入点表达式：知道对哪个类里面的哪个方法进行增强
   2. 语法结构：execution(\[权限修饰副\] \[返回类型\].\[方法名称\](\[参数列表\]))        
举例：
   * 星号（\*） 表示所有类型 第一个（\*）与后面之间有一个空格
      1. execution(* org.lee.study.spring.aop.dynamic.proxy.aspectj.Demo.method(...))
      1. execution(\* org.lee.study.spring.aop.dynamic.proxy.aspectj.\*.\*(...))  这个表示aspectj包下面的所有类和所有方法


## 2. 实操aspectJ
1. 创建被增强类（User）和增强类(UserProxy)
2. 进行通知的配置
   1. 在spring 文件中开启注解扫描
   2. 使用注解创建User和UserProxy对象  这个就是给两个对象加上@Component注解，这样在初始化上下文的时候上下文回去扫描我们的包下面的class文件来创建
   3. 在增强类上面添加注解@Aspect  
   4. 在spring配置文件中开始生成代理对象 添加 \<aop:aspectj-autoproxy\></aop:aspectj-autoproxy> 标签
3. 配置不同类型的通知
4. 相同的切入点抽取 @PointCut注解的使用
5. 如果有多个增强类对同一个方法进行增强，设置增强优先级，
   1. 在增强类的上面添加注解@Order(数字)， 数值越小优先级越高
   2. 高优先级回复覆盖低优先级而是在高优先级前面。
## AOP