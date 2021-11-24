# spring IOC
```java
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

public class org.lee.study.Main {
    public static void main(String[] args) {
        ClassPathResource res = new ClassPathResource("beans.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        int i = reader.loadBeanDefinitions(res);
        Object bean_test = factory.getBean("bean_test");
        System.out.println(bean_test);
        bean_test = factory.getBean("bean_test");
        System.out.println(bean_test);
    }
}
```
# 重要的工厂类 DefaultListableBeanFactory

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
		implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {xxxxxxx}
		
## 加载BeanDefinition        
说明，         
方法调用格式：     
    类名 # 调用的方法名称(参数1, 参数2 ...)
    如果没有类名那么就与同级(同一个调用方法)的前一个或者, 上级(被调用方法的调用方法)的前一个类名相同
 
<pre>
loadBeanDefinitions     
    XmlBeanDefinitionReader # loadBeanDefinitions(Resource resource)            
        XmlBeanDefinitionReader # loadBeanDefinitions(EncodedResource encodedResource):
            获取resource:		Set<pre> currentResources = this.resourcesCurrentlyBeingLoaded.get();
            XmlBeanDefinitionReader # doLoadBeanDefinitions(InputSource inputSource, Resource resource)
                # 加载文档，或者说加载资源，定义bean的资源
                XmlBeanDefinitionReader # doLoadDocument(InputSource inputSource, Resource resource) 
                    DefaultDocumentLoader # Document loadDocument(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler, int validationMode, boolean namespaceAware) 
                        DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory,Entity Resolver entityResolver, ErrorHandler errorHandler)
                        builder.parse(inputSource) // 这里返回了资源文档的解析
                XmlBeanDefinitionReader # registerBeanDefinitions(Document doc, Resource resource)  // 这里注册 BeanDefinition 的实例，并且返回新注册的个数
                    createBeanDefinitionDocumentReader(); // 通过反射执行创建一个reader实例
                        BeanUtils.instantiateClass(this.documentReaderClass);
                    documentReader.registerBeanDefinitions(doc, createReaderContext(resource)); // 真实调用：DefaultBeanDefinitionDocumentReader # registerBeanDefinitions(Document doc, XmlReaderContext readerContext)
                         BeanDefinitionDocumentReader # doRegisterBeanDefinitions(Element root)
                            BeanDefinitionDocumentReader # preProcessXml(root); // 空方法
                            BeanDefinitionDocumentReader # parseBeanDefinitions(root, this.delegate); // 解析bean的过程，并在解析完成的时候将bean注册
                                BeanDefinitionReaderUtils # registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
                                BeanDefinitionRegistry # registerBeanDefinition(String beanName, BeanDefinition beanDefinition) # 在 DefaultListableBeanFactory 的实现类里面他是将两个参数存放在了 beanDefinitionMap 的成员变量里面，这里的比较关键，后面的依赖注入里面会从里面获取比较重要的bean的成员变量定义信息
                                    // 两个主要的注册，1.beanDefinition和beanDefinitionNames 一个是bean的成员变量的值， 一个是bean的名称，已经注册了的bean的名称，这个暂时还不知道用处是什么意思
                                    this.beanDefinitionMap.put(beanName, beanDefinition);
                                    this.beanDefinitionNames.add(beanName);
                                    removeManualSingletonName(beanName);// 移除单例的bean 这个暂时不知道什么意思
                            BeanDefinitionDocumentReader # postProcessXml(root); // 空方法
                            
                
                    
                    
                        
                        
</pre>
        
    
DefaultListableBeanFactory#getBean