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



# AOP

