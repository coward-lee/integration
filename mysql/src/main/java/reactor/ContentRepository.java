package reactor;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ContentRepository extends ReactiveCrudRepository<Content, Long> {


    @Query("SELECT * FROM sharding_jdbc_01.shading_table")
    Flux<Content> queryAll();
}
