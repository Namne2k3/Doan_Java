package blog_spring.blog_spring.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "watch_attributes")
public class Watch_Attributes {
    @Id
    private String id;

    @DBRef
    private Product product;

    private String screen_tech;

    private String screen_size;

    private String resolution;

    private String diameter;

    private String design;

    private double weight;

    private int ram;

    private int memory;

    private double time_change;

    private String battery_life;
}
