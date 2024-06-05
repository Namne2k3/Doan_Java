package blog_spring.blog_spring.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "product_attributes")
public class Laptop_Attributes {
    @Id
    private String id;

    @DBRef
    private Product product;

    private String graphic;

    private String cpu;

    private int ram;

    private Integer ssd;

    private String panel;

    private double screen_size;

    private Integer brightness;

    private String resolution;

    private double weight;
}
