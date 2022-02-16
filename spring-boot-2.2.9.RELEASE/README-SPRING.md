# new SpringApplication(primarySources)
现在SpringApplication构造方法，主要是对
- 1.resourceLoader
- 2.primaryResources
- 3.webApplicationType 
- 4.intializers
- 5.listeners
- 6.mainApplicationClass        
五个成员变量的赋值，      
其中4、5 需要使用spring.factories中的一些属性            
代码如下：
```java
public class SpringApplication {
	public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
		// resourceLoader 设置为null(资源加载器)
		this.resourceLoader = resourceLoader;
		Assert.notNull(primarySources, "PrimarySources must not be null");
		// 将我们的主类放到这个成员成员变量
		this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
		// 推到web应用的类型，是servlet还是reactive
		this.webApplicationType = WebApplicationType.deduceFromClasspath();
		// 从 jar包和自己项目中的 META-INF/spring.factories 中的值拿出来,
		// 这个是拿去ApplicationListener全类名为key的属性值, 并赋值给 initializers
		setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
		// 从 jar包和自己项目中的 META-INF/spring.factories 中的值拿出来，
		// 这个是拿去ApplicationListener全类名为key的属性值, 并赋值给 listeners
		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
		this.mainApplicationClass = deduceMainApplicationClass();
	}
}
```
### 1. resourceLoader
### 2.primaryResources
### 3.webApplicationType 

* WebApplicationType.deduceFromClasspath()
```java

public enum WebApplicationType {
    NONE,SERVLET,REACTIVE;

	private static final String[] SERVLET_INDICATOR_CLASSES = { "javax.servlet.Servlet",
			"org.springframework.web.context.ConfigurableWebApplicationContext" };
	private static final String WEBMVC_INDICATOR_CLASS = "org.springframework.web.servlet.DispatcherServlet";
	private static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";
	private static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";

	static WebApplicationType deduceFromClasspath() {
		if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)
				&& !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
			return WebApplicationType.REACTIVE;
		}
		for (String className : SERVLET_INDICATOR_CLASSES) {
			if (!ClassUtils.isPresent(className, null)) {
				return WebApplicationType.NONE;
			}
		}
		return WebApplicationType.SERVLET;
	}
}
```
### 4.intializers
* setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
这个方法我们在下面一起讲
### 5.listeners
* setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));

```java
public class SpringApplication {
	private <T> Collection<T> getSpringFactoriesInstances(Class<T> type) {
		return getSpringFactoriesInstances(type, new Class<?>[] {});
	}

	private <T> Collection<T> getSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args) {
		ClassLoader classLoader = getClassLoader();
		// Use names and ensure unique to protect against duplicates
		// 这个是获取工厂类的全类名 SpringFactoriesLoader.loadFactoryNames(type, classLoader) 是去加载spring.factories文件中的全类名
		Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));
        // 通过反射创建工厂实例
		List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);
        // 排序
		AnnotationAwareOrderComparator.sort(instances);
		return instances;
	}

    // 创建bean工厂的实例，并把列表返回
	@SuppressWarnings("unchecked")
	private <T> List<T> createSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes,
			ClassLoader classLoader, Object[] args, Set<String> names) {
		List<T> instances = new ArrayList<>(names.size());
		for (String name : names) {
			try {
				Class<?> instanceClass = ClassUtils.forName(name, classLoader);
				Assert.isAssignable(type, instanceClass);
				Constructor<?> constructor = instanceClass.getDeclaredConstructor(parameterTypes);
				T instance = (T) BeanUtils.instantiateClass(constructor, args);
				instances.add(instance);
			}
			catch (Throwable ex) {
				throw new IllegalArgumentException("Cannot instantiate " + type + " : " + name, ex);
			}
		}
		return instances;
	}
}
```


```java

public final class SpringFactoriesLoader {

	public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
    // 获取我们的
    public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
		String factoryTypeName = factoryType.getName();
		return loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
	}
    // 这个是去获取所有工程项目下面的 META-INF/spring.factories 文件中的 所有属性值
	private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
		MultiValueMap<String, String> result = cache.get(classLoader);
		if (result != null) {
			return result;
		}

		try {
			Enumeration<URL> urls = (classLoader != null ?
					classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
					ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
			result = new LinkedMultiValueMap<>();
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				UrlResource resource = new UrlResource(url);
				Properties properties = PropertiesLoaderUtils.loadProperties(resource);
				for (Map.Entry<?, ?> entry : properties.entrySet()) {
					String factoryTypeName = ((String) entry.getKey()).trim();
					for (String factoryImplementationName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
						result.add(factoryTypeName, factoryImplementationName.trim());
					}
				}
			}
			cache.put(classLoader, result);
			return result;
		}
		catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load factories from location [" +
					FACTORIES_RESOURCE_LOCATION + "]", ex);
		}
	}
}
```
### 6.mainApplicationClass 

# 2 run(args) 方法解析
1. 获取并启动监听器，
2. 构造上下文环境
3. 初始化应用上下文，在创建这个应用上下文的收这里会去创建IOC容器,
   IOC会以context的属性存在容器中，这个容器的名称就是beanFactory
4. 刷新上下文的准备阶段, 主要是对应用上下文的一些属性给加上去，
   以及将我们的核心启动类（就是我们的springboot启动类）给加载到我们IOC容器
5. 刷新应用上下文 // 这里的工作主要都是交由spring来完成了，springboot 主要是在之前主要是准备 spring 的 context 了        
    &nbsp;&nbsp;&nbsp;    5.1. 准备环境  prepareRefresh();\
    &nbsp;&nbsp;&nbsp;    5.2. 准备Bean工厂  prepareBeanFactory(beanFactory);       \
    &nbsp;&nbsp;&nbsp;    5.3. 这个是主线 invokeBeanFactoryPostProcessors(beanFactory);      \
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  5.3.1. 检查我们的启动主类是不是     \
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  5.3.2. 执行ComponentScan包下面    \
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;     这个有一个很重要的方法 ConfigurationClassParser#doProcessConfigurationClass  \
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;     // 这个不是全部内容需要补充一下，不过是主要内容   \
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;    5.3.2.1 扫描类对里面的每一个类扫面里面的注解里面有没有在Component注解（如果没有会使用递归去解析）       \
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;    5.3.2.2 解析 @PropertySources注解        \
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;    5.3.2.3 解析 @Import     \
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;    5.3.2.4 解析 @ImportResource     
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;    5.3.2.5 解析 @Bean       
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;    5.3.2.6 对接口的默认方法的初始化，主要是为是实佩java      \
    &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;    5.3.2.7 看是否有父类，如果有父类那么返回父类，将父类执行3.2.x的操作 \
    &nbsp;&nbsp;&nbsp;   5.4.注册后置bean工厂，这里会有关于web的一些bean初始化等  registerBeanPostProcessors(beanFactory); \
    &nbsp;&nbsp;&nbsp;   5.5. 后面是一些监听结果的完成和模板方法的调用  \
6. 刷新应用上下文后的拓展接口, 默认是空实现，什么都没有做, 这个可以实现一些自定义功能

```java

public class SpringApplication {
	public ConfigurableApplicationContext run(String... args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ConfigurableApplicationContext context = null;
		Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
		configureHeadlessProperty();
		// 1. 获取并启动监听器
		SpringApplicationRunListeners listeners = getRunListeners(args);
		listeners.starting();
		try {
			// 2. 构造上下文环境
			ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
			ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
			// 处理需要忽略的Bean
			configureIgnoreBeanInfo(environment);
			// 打印Banner 答应spring 的那个文字图片
			Banner printedBanner = printBanner(environment);
			// 3. 初始化应用上下文，在创建这个应用上下文的收这里会去创建IOC容器,IOC会以context的属性存在容器中，
			// 	  这个容器的名称就是beanFactory
			context = createApplicationContext();
			// 实例化SpringBootExceptionReporter.class, 用来支持报告关于启动的错误
			exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
					new Class[] { ConfigurableApplicationContext.class }, context);
			// 4. 刷新上下文的准备阶段, 主要是对应用上下文的一些属性给加上去，
			// 	  以及将我们的核心启动类（就是我们的springboot启动类）给加载到我们IOC容器
			prepareContext(context, environment, listeners, applicationArguments, printedBanner);
			// 5. 刷新应用上下文 // 这里的工作主要都是交由spring来完成了，
			// 	  springboot 主要是在之前主要是准备 spring 的 context 了
			refreshContext(context);
			// 6. 刷新应用上下文后的拓展接口, 默认是空实现，什么都没有做, 这个可以实现一些自定义功能
			afterRefresh(context, applicationArguments);
			// end
			stopWatch.stop();
			if (this.logStartupInfo) {
				new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
			}
			listeners.started(context);
			callRunners(context, applicationArguments);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, listeners);
			throw new IllegalStateException(ex);
		}

		try {
			listeners.running(context);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, null);
			throw new IllegalStateException(ex);
		}
		return context;
	}
}
```

### 1. 获取并启动监听器，并且配置启动的profile

### 2. 构造上下文环境

* ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
* 这里回去解析从我们自己写的启动启动类的上面 main 函数的 args 解析,并封装到 CommandLineArgs 类里面， 
* 再将 CommandLineArgs 封装成 PropertySource 的子类 DefaultApplicationArguments$Source
```java
public class DefaultApplicationArguments implements ApplicationArguments {
	private final Source source;
	private final String[] args;

	public DefaultApplicationArguments(String... args) {
		Assert.notNull(args, "Args must not be null");
		this.source = new Source(args);
		this.args = args;
	}
	private static class Source extends SimpleCommandLinePropertySource {
		Source(String[] args) {
			super(args);
		}
	}
}

public class SimpleCommandLinePropertySource extends CommandLinePropertySource<CommandLineArgs> {
	public SimpleCommandLinePropertySource(String... args) {
		super(new SimpleCommandLineArgsParser().parse(args));
	}
}

class SimpleCommandLineArgsParser {
	public CommandLineArgs parse(String... args) {
		CommandLineArgs commandLineArgs = new CommandLineArgs();
		for (String arg : args) {
			if (arg.startsWith("--")) {
				String optionText = arg.substring(2);
				String optionName;
				String optionValue = null;
				int indexOfEqualsSign = optionText.indexOf('=');
				if (indexOfEqualsSign > -1) {
					optionName = optionText.substring(0, indexOfEqualsSign);
					optionValue = optionText.substring(indexOfEqualsSign + 1);
				}else {
					optionName = optionText;
				}
				if (optionName.isEmpty()) {
					throw new IllegalArgumentException("Invalid argument syntax: " + arg);
				}
				commandLineArgs.addOptionArg(optionName, optionValue);
			}
			else {
				commandLineArgs.addNonOptionArg(arg);
			}
		}
		return commandLineArgs;
	}
}
```
* ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
* todo 
```java
public class SpringApplication {
	private ConfigurableEnvironment prepareEnvironment(SpringApplicationRunListeners listeners,
			ApplicationArguments applicationArguments) {
		// Create and configure the environment
		ConfigurableEnvironment environment = getOrCreateEnvironment();
		configureEnvironment(environment, applicationArguments.getSourceArgs());
		ConfigurationPropertySources.attach(environment);
		listeners.environmentPrepared(environment);
		bindToSpringApplication(environment);
		if (!this.isCustomEnvironment) {
			environment = new EnvironmentConverter(getClassLoader()).convertEnvironmentIfNecessary(environment,
					deduceEnvironmentClass());
		}
		ConfigurationPropertySources.attach(environment);
		return environment;
	}
}
```

### 3. 初始化应用上下文，在创建这个应用上下文的收这里会去创建IOC容器,IOC会以context的属性存在容器中，这个容器的名称就是beanFactory

* 利用反射是实例化对应的上下文对象
```java
public class SpringApplication {
	public static final String DEFAULT_SERVLET_WEB_CONTEXT_CLASS = "org.springframework.boot."
			+ "web.servlet.context.AnnotationConfigServletWebServerApplicationContext";
	protected ConfigurableApplicationContext createApplicationContext() {
		Class<?> contextClass = this.applicationContextClass;
		if (contextClass == null) {
			try {
				switch (this.webApplicationType) {
				case SERVLET:
					contextClass = Class.forName(DEFAULT_SERVLET_WEB_CONTEXT_CLASS);
					break;
				case REACTIVE:
					contextClass = Class.forName(DEFAULT_REACTIVE_WEB_CONTEXT_CLASS);
					break;
				default:
					contextClass = Class.forName(DEFAULT_CONTEXT_CLASS);
				}
			}
			catch (ClassNotFoundException ex) {
				throw new IllegalStateException(
						"Unable create a default ApplicationContext, please specify an ApplicationContextClass", ex);
			}
		}
		return (ConfigurableApplicationContext) BeanUtils.instantiateClass(contextClass);
	}
}
// IOC容器的存放位置
public class AnnotationConfigServletWebServerApplicationContext extends ServletWebServerApplicationContext
		implements AnnotationConfigRegistry {
	private final AnnotatedBeanDefinitionReader reader;
	private final ClassPathBeanDefinitionScanner scanner;
	private final Set<Class<?>> annotatedClasses = new LinkedHashSet<>();
	private String[] basePackages;
}
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {
    // 这个就是IOC容器：BeanFactory
	private final BeanDefinitionRegistry registry;
}
```
* // 实例化SpringBootExceptionReporter.class, 用来支持报告关于启动的错误
// 这个又是从spring.factories中去获取对应的全类名，并返回实例化后的对象
exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
        new Class[] { ConfigurableApplicationContext.class }, context);
        
### 4. 刷新上下文的准备阶段, 
* 主要是对应用上下文的一些属性给加上去，就是将context的成员变量赋值
1. 将我们之前初始化的environment赋值给context的成员变量
2. 向各个监听器发送容易已经**准备**好的事件
3. 把main函数中的args封装成单例bean
4. 以及将我们的核心启动类（就是springboot启动类）给加载到我们IOC容器      
    这里较为负复杂的方法调用为：load(context, sources.toArray(new Object[0]));
5. 向各个监听器发送容易已经**加载**好的事件
```java
public class SpringApplication {
	private void prepareContext(ConfigurableApplicationContext context, ConfigurableEnvironment environment,
			SpringApplicationRunListeners listeners, ApplicationArguments applicationArguments, Banner printedBanner) {
		// 设置environment
		context.setEnvironment(environment);
		// todo  需要看一下 
		// 设置 :: context.getBeanFactory().setConversionService(ApplicationConversionService.getSharedInstance());
		postProcessApplicationContext(context);
		// 执行初始化，这里有一个后置处理器的初始化
		applyInitializers(context);
		// 向各个监听器发送容易已经准备好的事件
		listeners.contextPrepared(context);
		if (this.logStartupInfo) {
			logStartupInfo(context.getParent() == null);
			logStartupProfileInfo(context);
		}
		// Add boot specific singleton beans
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		// 把main函数中的args封装成单例bean
		beanFactory.registerSingleton("springApplicationArguments", applicationArguments);
		if (printedBanner != null) {
			beanFactory.registerSingleton("springBootBanner", printedBanner);
		}
		if (beanFactory instanceof DefaultListableBeanFactory) {
			((DefaultListableBeanFactory) beanFactory)
					.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
		}
		if (this.lazyInitialization) {
			context.addBeanFactoryPostProcessor(new LazyInitializationBeanFactoryPostProcessor());
		}
		// Load the sources
		// 这里获取到的就是我们获取到的核心启动类
		Set<Object> sources = getAllSources();
		Assert.notEmpty(sources, "Sources must not be empty");
		// 将我们的核心启动类注册到IOC的bean容器中
		load(context, sources.toArray(new Object[0]));
		listeners.contextLoaded(context);
	}
}
```

* 解析： load(context, sources.toArray(new Object[0])); 的执行过程

1. 创建 BeanDefinitionLoader 类的 loader 对象，并使用该对象的load()       
2. 由于我们传入的我们的启动类，所以回去load我们的启动类，        
   所谓的load就是将类实例化，如果需要就加入的IOC容器中，就是注册到beanFactory中的容器中      
* 注册到IOC容器中     
将启动类的 BeanDefinition 注册到 beanDefinitionMap中     
DefaultListableBeanFactory # registerBeanDefinition(String,BeanDefinition)      
```java
public class SpringApplication {
	protected void load(ApplicationContext context, Object[] sources) {
		if (logger.isDebugEnabled()) {
			logger.debug("Loading source " + StringUtils.arrayToCommaDelimitedString(sources));
		}
		BeanDefinitionLoader loader = createBeanDefinitionLoader(getBeanDefinitionRegistry(context), sources);
		if (this.beanNameGenerator != null) {
			loader.setBeanNameGenerator(this.beanNameGenerator);
		}
		if (this.resourceLoader != null) {
			loader.setResourceLoader(this.resourceLoader);
		}
		if (this.environment != null) {
			loader.setEnvironment(this.environment);
		}
		loader.load();
	}
	protected BeanDefinitionLoader createBeanDefinitionLoader(BeanDefinitionRegistry registry, Object[] sources) {
		return new BeanDefinitionLoader(registry, sources);
	}
}
class BeanDefinitionLoader{
	BeanDefinitionLoader(BeanDefinitionRegistry registry, Object... sources) {
		Assert.notNull(registry, "Registry must not be null");
		Assert.notEmpty(sources, "Sources must not be empty");
		this.sources = sources;
		this.annotatedReader = new AnnotatedBeanDefinitionReader(registry);     // 注解形式的Bean定义读取器
		this.xmlReader = new XmlBeanDefinitionReader(registry);					// xml形式的
		if (isGroovyPresent()) {
			this.groovyReader = new GroovyBeanDefinitionReader(registry);
		}
		this.scanner = new ClassPathBeanDefinitionScanner(registry);			// 类路径扫描器
		this.scanner.addExcludeFilter(new ClassExcludeFilter(sources));			// 扫描器添加排除过滤器
	}
	int load() {
		int count = 0;
		for (Object source : this.sources) {
			count += load(source);
		}
		return count;
	}

	private int load(Object source) {
		Assert.notNull(source, "Source must not be null");
        // 这次调用我们会走这个if分支
		if (source instanceof Class<?>) {
			return load((Class<?>) source);
		}
		if (source instanceof Resource) {
			return load((Resource) source);
		}
		if (source instanceof Package) {
			return load((Package) source);
		}
		if (source instanceof CharSequence) {
			return load((CharSequence) source);
		}
		throw new IllegalArgumentException("Invalid source type " + source.getClass());
	}

	private int load(Class<?> source) { // source 是我们的主类，
		if (isGroovyPresent() && GroovyBeanDefinitionSource.class.isAssignableFrom(source)) {
			// Any GroovyLoaders added in beans{} DSL can contribute beans here
			GroovyBeanDefinitionSource loader = BeanUtils.instantiateClass(source, GroovyBeanDefinitionSource.class);
			load(loader);
		}
		if (isComponent(source)) {   // 判断这个source是不是被有Component，  @SpringBootConfiguration->@Configuration->@Component
            // 将启动类的 BeanDefinition 注册到 beanDefinitionMap中
            // 这个最后会去调用下面这个方法
            // DefaultListableBeanFactory # registerBeanDefinition(String,BeanDefinition)
			this.annotatedReader.register(source);		
			return 1;
		}
		return 0;
	}
}
```


### 5. 刷新应用上下文 // 这里的工作主要都是交由spring来完成了，springboot 主要是在之前主要是准备 spring 的 context 了        
 5.1. 准备环境  prepareRefresh();\
 5.2. 准备Bean工厂  prepareBeanFactory(beanFactory);       \
 5.3. 这个是主线 invokeBeanFactoryPostProcessors(beanFactory);      \
 5.3.1. 检查我们的启动主类是不是     \
 5.3.2. 执行ComponentScan包下面    \
    这个有一个很重要的方法 ConfigurationClassParser#doProcessConfigurationClass  \
    // 这个不是全部内容需要补充一下，不过是主要内容   \
     5.3.2.1 扫描类对里面的每一个类扫面里面的注解里面有没有在Component注解（如果没有会使用递归去解析）       \
     5.3.2.2 解析 @PropertySources注解        \
     5.3.2.3 解析 @Import     \
     5.3.2.4 解析 @ImportResource     
     5.3.2.5 解析 @Bean       
     5.3.2.6 对接口的默认方法的初始化，主要是为是实佩java      \
     5.3.2.7 看是否有父类，如果有父类那么返回父类，将父类执行3.2.x的操作 \
 5.4.注册后置bean工厂，这里会有关于web的一些bean初始化等  registerBeanPostProcessors(beanFactory); \
 5.5. 后面是一些监听结果的完成和模板方法的调用  \
6. 刷新应用上下文后的拓展接口, 默认是空实现，什么都没有做, 这个可以实现一些自定义功能

```java
public class SpringApplication {
	private void refreshContext(ConfigurableApplicationContext context) {
		refresh(context);
		if (this.registerShutdownHook) {
			try {
				context.registerShutdownHook();
			}
			catch (AccessControlException ex) {
				// Not allowed in some environments.
			}
		}
	}
	protected void refresh(ApplicationContext applicationContext) {
		Assert.isInstanceOf(AbstractApplicationContext.class, applicationContext);
		((AbstractApplicationContext) applicationContext).refresh();
	}
}


public abstract class AbstractApplicationContext extends DefaultResourceLoader
		implements ConfigurableApplicationContext {
	@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			prepareRefresh();

			// Tell the subclass to refresh the internal bean design.parttern.factory.
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean design.parttern.factory for use in this context.
			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean design.parttern.factory in context subclasses.
				postProcessBeanFactory(beanFactory);

				// Invoke design.parttern.factory processors registered as beans in the context.
				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);

				// Initialize message source for this context.
				initMessageSource();

				// Initialize event multicaster for this context.
				initApplicationEventMulticaster();

				// Initialize other special beans in specific context subclasses.
				onRefresh();

				// Check for listener beans and register them.
				registerListeners();

				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
			}
		}
	}
}
```