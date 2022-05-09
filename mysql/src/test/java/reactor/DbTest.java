package reactor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;


@SpringBootTest(classes = Main.class)
public class DbTest {
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    private R2dbcEntityTemplate template;
    @Test
    public void test_repository() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        contentRepository.findAll().subscribe(
                Content::toString,
                System.out::println,
                countDownLatch::countDown
                );
        countDownLatch.await();

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
//        contentRepository.queryAll().subscribe(content-> System.out.println("queryAll:"+content));
        Thread.sleep(Integer.MAX_VALUE);
    }

}
