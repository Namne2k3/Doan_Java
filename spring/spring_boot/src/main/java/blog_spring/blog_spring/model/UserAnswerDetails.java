package blog_spring.blog_spring.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "user_answer_detail")
@Data
public class UserAnswerDetails {

    private ObjectId question_id;
    private ObjectId choice_id;
    private Date answer_time;
}
