# @SpringBootApplication 注解的解析
## 1.  @SpringBootApplication 注解的原貌

```java
@Target(ElementType.TYPE)        // 只用范围可以作用类在类，接口，注解，或者枚举
@Retention(RetentionPolicy.RUNTIME)  // 注解的生命周期，Runtime运行时
@Documented  // 可以记录在javadoc中
@Inherited     // 可以被继承
@SpringBootConfiguration    //  表示该类为配置类，因为他 使用了 @Configuration注解，就是对该注解的一个重新包装
@EnableAutoConfiguration    //	启动自动配置功能
@ComponentScan(
		excludeFilters = {
				@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
				@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)
		}
)
public @interface SpringBootApplication {
```


## 1.1 @SpringBootConfiguration
1. 何时被加载解析的
2. 加载的流程
## 1.2 @EnableAutoConfiguration

## 1.3 @ComponentScan
    @ComponentScan(      
        excludeFilters = {              
                    @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),       
                    @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)       
            }   
    )   