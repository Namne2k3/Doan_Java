package blog_spring.blog_spring.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "question")
@Data
public class Question {

    @Id
    private ObjectId id;
    private String question_text;
    private List<Choice> choices;
}
