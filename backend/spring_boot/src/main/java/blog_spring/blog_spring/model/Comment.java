package blog_spring.blog_spring.model;

import lombok.Data;
import org.checkerframework.common.value.qual.IntRange;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "comments")
@Data
public class Comment {
    @Id
    private String id;

    @DBRef
    private User user;

    private String productId;

    @IntRange(from = 1, to = 5)
    private int rate;

    private String text;

    private List<String> images;
}
