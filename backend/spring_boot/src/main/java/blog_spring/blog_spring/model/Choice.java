package blog_spring.blog_spring.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "choice")
@Data
public class Choice {

    @Id
    private ObjectId id;
    private String choice_text;
    private boolean is_correct;
}
