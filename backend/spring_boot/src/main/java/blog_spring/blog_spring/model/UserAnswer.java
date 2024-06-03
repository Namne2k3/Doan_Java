package blog_spring.blog_spring.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user_answer")
@Data
public class UserAnswer {

    @Id
    private ObjectId id;
    private ObjectId user_id;
    private ObjectId quiz_id;
    private List<UserAnswerDetails> answers;
}
