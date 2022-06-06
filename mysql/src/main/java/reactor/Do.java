package reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class Do {
    private final static Logger log = LoggerFactory.getLogger(Do.class);

    private final JdbcTemplate jdbcTemplate;

    public Do(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insert();
    }
    public void insert(){
        IntStream.range(1,1000000).parallel().forEach(i->{
            String base = "insert into user(name) values ";
            StringBuilder result = new StringBuilder(base + " ");
            for (int j = 0; j < 100; j++) {
                result.append("('").append(i).append("name'),");
            }
            result.append("('"+i+"name')");
            jdbcTemplate.update(result.toString());
            log.info("inset i:{}",i);
        });

    }
}
