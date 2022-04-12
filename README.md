## gradle构建错误 
Could not resolve all dependencies for configuration ':detachedConfiguration13'.
Using insecure protocols with repositories, without explicit opt-in, is unsupported. Switch Maven repository 'maven(http://maven.aliyun.com/nexus/content/groups/public/)' to redirect to a secure protocol (like HTTPS) or allow insecure protocols. See https://docs.gradle.org/7.0.2/dsl/org.gradle.api.artifacts.repositories.UrlArtifactRepository.html#org.gradle.api.artifacts.repositories.UrlArtifactRepository:allowInsecureProtocol for more details. 


    解决办法
    maven{
        allowInsecureProtocol = true
        url 'http://maven.aliyun.com/nexus/content/groups/public/'
    }
    问题出处
    maven{
        url 'http://maven.aliyun.com/nexus/content/groups/public/'
    }
    
## git rebase -i xxx
交互式的rebase 
pick commit1   // 时间11.11
s commit2   // 时间11.12
s commit3   // 时间11.13
这里的提交全部都会汇聚到commit1里面

## 
    Build scan background action failed.
        org.gradle.process.internal.ExecException: Process 'command 'git'' finished with non-zero exit value 128
        at org.gradle.process.internal.DefaultExecHandle$ExecResultImpl.assertNormalExitValue(DefaultExecHandle.java:414)
        at org.gradle.process.internal.DefaultExecAction.execute(DefaultExecAction.java:38)
        at org.gradle.process.internal.DefaultExecActionFactory.exec(DefaultExecActionFactory.java:175)
        at io.spring.ge.conventions.gradle.WorkingDirectoryProcessOperations.exec(WorkingDirectoryProcessOperations.java:45)
        at io.spring.ge.conventions.gradle.ProcessOperationsProcessRunner.run(ProcessOperationsProcessRunner.java:41)
        at io.spring.ge.conventions.core.BuildScanConventions.run(BuildScanConventions.java:166)
        at io.spring.ge.conventions.core.BuildScanConventions.addGitMetadata(BuildScanConventions.java:113)
        at io.spring.ge.conventions.gradle.GradleConfigurableBuildScan.lambda$background$0(GradleConfigurableBuildScan.java:104)
        at com.gradle.enterprise.gradleplugin.internal.extension.DefaultBuildScanExtension$3.run(SourceFile:100)
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
        at java.util.concurrent.FutureTask.run(FutureTask.java:266)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)



# 无法构建父类变量的解决方案

@SuperBuilder 可以解决      
但是build需要注意一下顺序：**先构建子类，再构建父类的变量。**     
如果先构建父类，会无法构建子类    
```java
@SuperBuilder
class Parent{
    String parentId;
}
@SuperBuilder
class Child extends Parent{
    String childId;
}
class Demo{
    public static void main(String[] args) {
        // 出问题的构建方法
        Child.builder()
                .parentId("parent")
                .childId("childId") // 这里会直接导致编译不过
                .build();
        // 正确的构建方式
        Child.builder()
                .childId("childId") // 应该先构建子类的变量
                .parentId("parent")
                .build();
    }
}
```

## json 序列化注意点
这里有一个小细节，在将jackson转化对象到数据库中的一个字段的时候，他会根据getXXX() 方法序列化成为对应的字段。
    
    public class AssetLeafCategoryFilter implements AssetFilterItemContent {
        private String value;
    
        public AssetLeafCategoryFilter(String value) {
            this.value = value;
        }
    
        public String getValue() {
            return value;
        }
        public String getFiled() {
            return value;
        }
    }
最终结果为：{value:"xxx",filed:"xxx"}     
他会将 filed 也看作一个字段    




## gradle 编译scala错误
下面的 java.scrDirs = ['src/main/java'] 或者 ['src/main/scala'] 就会导致scala的类无法在编译的时候被找到，为什么，          
我的出现这个情况的上下文是，我的其中一个java类引用了scala的文件，不知道是不是因为配置了java.srcDirs就导致无法识别倒入的scala类了
```groovy
sourceSets {
    main() {
        scala{
            srcDirs = ['src/main/scala', 'src/main/java']
        }
        java {
            srcDirs = []
        }
    }
    test {
        scala{
            srcDirs = ['src/test/java', 'src/test/scala']
        }
        java {
            srcDirs = []
        }
    }
}
```

