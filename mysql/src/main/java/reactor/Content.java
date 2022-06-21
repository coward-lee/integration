package reactor;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("user")
public class Content {
    @Id
    private Long id;
    private String name;

    @Override
    public String toString() {
        return "Content{" +
                "id=" + id +
                ", content='" + name + '\'' +
                '}';
    }
}
