package org.lee.study.study;

//import org.springframework.beans.design.parttern.factory.BeanFactory;
//import org.springframework.beans.support.ResourceEditorRegistrar;
//import org.springframework.context.*;
//import org.springframework.context.expression.StandardBeanExpressionResolver;
//import org.springframework.context.support.ApplicationContextAwareProcessor;
//import org.springframework.context.support.ApplicationListenerDetector;
//import org.springframework.context.support.ContextTypeMatchClassLoader;
//import org.springframework.context.weaving.LoadTimeWeaverAwareProcessor;
//import org.springframework.core.NativeDetector;
//import org.springframework.core.io.ResourceLoader;

public class SpringDemo {
    public static void main(String[] args) {

//        // Tell the internal bean design.parttern.factory to use the context's class loader etc.
//        beanFactory.setBeanClassLoader(getClassLoader());
//        if (!shouldIgnoreSpel) {
//            beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
//        }
//        beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));
//
//        // Configure the bean design.parttern.factory with context callbacks.
//        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
//        beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
//        beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
//        beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
//        beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
//        beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
//        beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
//        beanFactory.ignoreDependencyInterface(ApplicationStartupAware.class);
//
//        // BeanFactory interface not registered as resolvable type in a plain design.parttern.factory.
//        // MessageSource registered (and found for autowiring) as a bean.
//        beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
//        beanFactory.registerResolvableDependency(ResourceLoader.class, this);
//        beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
//        beanFactory.registerResolvableDependency(ApplicationContext.class, this);
//
//        // Register early post-processor for detecting inner beans as ApplicationListeners.
//        beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));
//
//        // Detect a LoadTimeWeaver and prepare for weaving, if found.
//        if (!NativeDetector.inNativeImage() && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
//            beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
//            // Set a temporary ClassLoader for type matching.
//            beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
//        }
//
//        // Register default environment beans.
//        if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
//            beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
//        }
//        if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
//            beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
//        }
//        if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
//            beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
//        }
//        if (!beanFactory.containsLocalBean(APPLICATION_STARTUP_BEAN_NAME)) {
//            beanFactory.registerSingleton(APPLICATION_STARTUP_BEAN_NAME, getApplicationStartup());
//        }
    }
}
