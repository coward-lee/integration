## 1. spring boot 自定义 starter
1. 编写 resources/META-INF/spring.factories 文件
指定一个spring 自动配置类，这个springboot 的工厂后置处理器回去加载这个文件下的这个内容，并解析并且注册到ioc容器中
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.lee.autoconfigure.TestAutoConfiguration

2. org.lee.autoconfigure.TestAutoConfiguration
将这个类编写成为一个spring 的配置类
