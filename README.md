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