package blog_spring.blog_spring.model;

import lombok.Data;
import org.checkerframework.common.value.qual.IntRange;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Document(collection = "comments")
@Data
public class Comment {

    public Comment() {
        isVerify = false;
        createdAt = new Date();
    }

    @Id
    private String id;

    @DBRef
    private User user;

    private String productId;

    @IntRange(from = 1, to = 5)
    private int rate;

    private String text;

    private List<String> images;

    private boolean isVerify;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;
}
