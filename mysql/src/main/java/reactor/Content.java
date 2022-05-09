package reactor;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("shading_table")
public class Content {
    @Id
    private Long id;
    private String content;

    @Override
    public String toString() {
        return "Content{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
