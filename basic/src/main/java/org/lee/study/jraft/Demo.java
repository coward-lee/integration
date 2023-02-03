package org.lee.study.jraft;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.JRaftUtils;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.util.Endpoint;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;

public class Demo {

    /**
     * 地址 Endpoint
     */
    @Test
    public void test_endpoint() {
        Endpoint addr = new Endpoint("localhost", 8080);
        System.out.println(addr.toString());
        PeerId peer = new PeerId();
        boolean parse = peer.parse(addr.toString());
        System.out.println(parse);
    }

    /**
     * 节点 PeerId
     */
    @Test
    public void test_peer() {
        PeerId peer = new PeerId("localhost", 8080);
        Endpoint addr = peer.getEndpoint(); // 获取节点地址
        System.out.println(addr.toString());
        int index = peer.getIdx(); // 获取节点序号，目前一直为 0
        System.out.println(index);
        String s = peer.toString(); // 结果为 localhost:8080
        boolean success = peer.parse(s);  // 可以从字符串解析出 PeerId，结果为 tr
        System.out.println(success);
    }

    /**
     * Configuration
     * Configuration 表示一个 raft group 的配置，也就是参与者列表：
     * 分组
     */

    @Test
    public void test_configuration() {
        PeerId peer1 = new PeerId("localhost", 8080);
        PeerId peer2 = new PeerId("localhost", 8081);
        PeerId peer3 = new PeerId("localhost", 8082);
// 由 3 个节点组成的 raft group
        Configuration conf = new Configuration();
        conf.addPeer(peer1);
        conf.addPeer(peer2);
        conf.addPeer(peer3);
        System.out.println(conf);
    }

    @Test
    public void test_util() {
        Endpoint addr = JRaftUtils.getEndPoint("localhost:8080");
        PeerId peer = JRaftUtils.getPeerId("localhost:8080");
// 三个节点组成的 raft group 配置，注意节点之间用逗号隔开
        Configuration conf = JRaftUtils.getConfiguration("localhost:8081,localhost:8082,localhost:8083");
        System.out.println(conf);
    }

    @Test
    public void test_closure() throws InterruptedException {
        Closure done = System.out::println;
        Task task = new Task();
        task.setData(ByteBuffer.wrap("hello".getBytes()));
        task.setDone(done);
        Thread.sleep(1000);
    }

    @Test
    public void test_iterator() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10);
//        Iterator it = List.of(byteBuffer).iterator();
//         遍历迭代任务列表
//        while (it.hasNext()) {
//            ByteBuffer data = it.getData(); // 获取当前任务数据
//            Closure done = it.done();  // 获取当前任务的 closure 回调
//            long index = it.getIndex();  // 获取任务的唯一日志编号，单调递增， jraft 自动分配
//            long term = it.getTerm();  // 获取任务的 leader term
//              ...逻辑处理...
//            it.next(); // 移到下一个task
//        }
    }
}

