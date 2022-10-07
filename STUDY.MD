
# gradle
<pre>
命令
gradle clean 清理build目录
gradle classes 编译业务代码和配置文件
gradle test 编译测试代码，生成测试报告
gradle build 构建项目
gradle build -x test 跳过测试代码
</pre>
- 依赖相关
直接依赖： 依赖类型 组名 名称 版本号    
本地依赖 本地的某个jar包 具体可以通过 文件集合、文件树的方式指定  
项目依赖： 当前项目的模块  

在依赖过程中被重复依赖的任务只会执行一次  

- 依赖类型  


| 类型                 | 提供者                 | 描述                                                   |  
|--------------------|---------------------|------------------------------------------------------|  
| compilyOnly        | java插件             | 短暂叫过provided，后面改成了compileOnly                        |
| runtimeOnly        | java插件             | 只在运行期有效，比如mysql驱动包，取代老版本中的runtime                    |
| implementation     | java插件             | 针对 ["src/main 目录"], 在编译时需要， 取代compile                |
| testCompileOnly    | java插件             | 编译测试的依赖项，运行时不需要                                      |
| testRuntimeOnly    | java插件             | 测试运行时需要， 取代testRuntime                               |
| testImplementation | java插件             | 针对 scr/test 目录 取代老版本的 testCompile                    |
| providedCompile    | war插件              | 编译、测试阶段代码需要依赖此类jar包，而运行阶段容器以及提供相应的支持，所以无需将这些文件打入war包 |
| compile            | xx                  | 编译范围在所有的classpath中可用，同时也会被打包。在 7.0中被移除               |
| runtime            | xx                  | 依赖在运行和测试系统的时候需要，在编译的时候不需要，比如说mysql驱动包                |
| api                | java-library插件     | 这些依赖项可以传递性地导出给使用者，用于编译和运行时。取代老版本中被移除的compile         |
| compileOnlyApi     | java-library插件     | 声明模块和使用者在编译时需要依赖项，但运行时不需要                            |

implementation 和 api的区别  
implementation 不会传递依赖  
api 会  
在需要重复依赖同一个模块的时候使用 下层的模块使用 api  
如   
A : B C    
B : C    
A implementation B  
B api C  


- 依赖冲突以及解决方案
1. 默认是使用高版本的依赖  
2. 通过 implementation("xxx") exclude{ xxx } 排除
3. 通过 implementation("xxx") {transitive(false)} 不进行依赖传递    不建议使用这种方式
4. 通过 implementation("xxx:1.1.0!!") 通过两个 ! 强制指定1.1.0 这个版本号   
 或者 implementation("xxx:1.1.0"){version{strict("1.4.0")}

查看依赖冲突
