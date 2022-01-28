
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


