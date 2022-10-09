package org.lee;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;

class HDFSClient {


    FileSystem fs;
    @BeforeEach
    void init() throws Throwable {

        // 船舰集群nn地址
        URI uri = new URI("hdfs://hadoop102:8020");
        // 创建配置文件
        Configuration configuration = new Configuration();
        configuration.set("dfs.replication", "2");

        // 用户
        String user = "root";
        // 1. 获取到了客户端对象
        fs = FileSystem.get(uri, configuration, user);
    }

    @AfterEach
    void close() throws IOException {
        // 3. 关闭资源
        fs.close();
    }

    /**
     * 创建文件夹
     */
    @Test
    void test_mkdir() throws Throwable {
        // 2. 创建文件夹
        fs.mkdirs(new Path("/xiyou/huaguoshan"));
    }

    /**
     * 优先级 ： hdfs-default.xml < 服务器端 hdfs-site.xml < 客户端 hdfs-site.xml < 客户端 Configuration 类的定义配置
     * 上传文件
     */
    @Test
    void test_put() throws Throwable {
        // 参数解读: 参数一： 表示删除原数据， 参数二： 是否允许覆盖， 参数三：原数据路劲； 参数四： 目的路径
        fs.copyFromLocalFile(false, true, new Path("D://sunwukong.txt"),  new Path("hdfs://hadoop102/xiyou/huaguoshan"));
    }
    /**
     * 优先级 ： hdfs-default.xml < 服务器端 hdfs-site.xml < 客户端 hdfs-site.xml < 客户端 Configuration 类的定义配置
     * 下载文件
     */
    @Test
    void test_get() throws Throwable {
        // 参数解读: 参数一： 表示删除原文件， 参数二： 源文件hdfs路径， 参数三：目标地址（代码运行的本地）路径； 参数四： ?
        fs.copyToLocalFile(false,  new Path("hdfs://hadoop102/xiyou/huaguoshan/sunwukong.txt"),  new Path("D:\\"), false);
    }
}
