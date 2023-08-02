
# BeanFactory 的核心接口和实现类
* ListableBeanFactory
* AutowireCapableBeanFactory
* AbstractBeanFactory
* DefaultListableBeanFactory

# FactoryBean 工厂Bean
 在配置文件配置Bean的时候，配置的bean和返回类型可以不一样，而是通过FactoryBean的getObject方法来返回对应的Bean
 
# Bean的作用域
* scope属性 singleton和prototype
* 如果bean为singleton的话这bean的实例会保存一份到singleObjects(这个是一个map)中，
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
1. 横切关注点 是我们的业务代码或者日志和事务等都可能是横切关注点
2. 连接点(join point)：类横切关注点。连接点可能是类初始化、方法执行、方法调用、字段调用或处理异常等等，Spring只支持方法执行连接点，在AOP中表示为在哪里干；
3. 切入点(point cut)：实际被增强的方法，成为切入点,就是我们自己写的业务代码（这个可能不只一个方法，可能是堆方法）
4. 通知（增强）(advice)：实际增强的逻辑部分称之为通知（增强），（连接点上的行为），也就是非业务代代码
   前置通知、后置通知、环绕通知、异常通知、最终通知
5. 切面是动作(aspect)：横切关注点就被模块化到特殊的类里——这样的类我们通常就称之为“切面”。
6. 引入 （inter-type declaration）: 也称为内部类型声明，为已有的类添加额外新的字段或方法，Spring允许引入新的接口（必须对应一个实现）到所有被代理对象（目标对象）, 在AOP中表示为干什么（引入什么）；
7. 织入（weaving）：：把切面连接到其它的应用程序类型或者对象上，并创建一个被通知的对象。这些可以在编译时（例如使用AspectJ编译器），类加载时和运行时完成。Spring和其他纯Java AOP框架一样，在运行时完成织入。在AOP中表示为怎么实现的；
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
#### AOP对象的创建

#### AOP对象的调用
aop 执行流程图解
![](D:\code\java\integration\spring\img\aop.png)
ReflectiveMethodInvocation有一个成员变量：interceptorsAndDynamicMethodMatchers
这个就是advice的列表，在执行的时候就是去迭代这个列表中的成员并且invoke 相应的方法（如AspectJAroundAdvice#invoke）
对象有一个成员变量

## spring 推荐使用构造体注入
依赖不可变：其实说的就是final关键字，这里不再多解释了。不明白的园友可以回去看看Java语法。     
依赖不为空（省去了我们对其检查）：当要实例化FooController的时候，由于自己实现了有参数的构造函数，所以不会调用默认构造函数，那么就需要Spring容器传入所需要的参数，所以就两种情况：    
&emsp;&emsp;1：有该类型的参数->传入，OK 。      
&emsp;&emsp;2：无该类型的参数->报错。所以保证不会为空，Spring总不至于传一个null进去吧 😦         
完全初始化的状态：这个可以跟上面的依赖不为空结合起来，向构造器传参之前，要确保注入的内容不为空，那么肯定要调用依赖组件的构造方法完成实例化。而在Java类加载实例化的过程中，构造方法是最后一步（之前如果有父类先初始化父类，然后自己的成员变量，最后才是构造方法，这里不详细展开。）。所以返回来的都是初始化之后的状态。     



## 构造体注入 依赖循环解决
1. 实例化对象的处理，核心调用方法  
一些核心方法如下
 - DefaultListableBeanFactory#resolveDependency
 - ContextAnnotationAutowireCandidateResolver#getLazyResolutionProxyIfNecessary
 - ContextAnnotationAutowireCandidateResolver#buildLazyResolutionProxy
这一步会给代理对象set一个匿名的TargetSource对象，这个是实现可以换选调用的关键所在
 - ObjenesisCglibAopProxy#createProxyClassAndInstance
 - SpringObjenesis#newInstance  
构造器参数的实例化过程，果构造参数是被lazy注解注释了，那么他会创建一个代理对象这个代理对象会调用的构造器方法传入参数（(Object[])null）来实例化参数，    
这个被创建出来的bean是一个被cglib代理了的一个bean， 每次在执行的时候回去执行相应的代理动作

2. 通过构造器参数赋值的成员变量(bean)的调用过程
由于是被cglib 代理过了的对象，每次调用方法之前会想执行cglib的代理对象的代码，
而最终调用道德java代码是DynamicAdvisedInterceptor#intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)   
参数逐个解释，proxy对象是就是发起调用的(成员变量)bean，method就是我们具体调用的方法，args参数，methodProxy 被cglib代理了的方法，  
在执行过程中，会先去singleObjects的map中查出map中的和proxy对象一样的bean，实际的调用都是从map中的bean来进行调用的。  


3. 如果scope为prototype 同样可以解决，prototype是在获取bean的时候创建bean同时创建构造器参数，此时的构造器参数还是会被代理，   
但是如果此时的构造器参数再去调用的时候对应的方法是不是去singletonObjects map中取对象而是new一个新的对象出来

## spring 中 相同的beanName 但是类型不同
会冲突，初始化spring环境的时候就会报错

