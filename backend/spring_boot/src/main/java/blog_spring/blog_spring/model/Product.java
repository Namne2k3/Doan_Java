package blog_spring.blog_spring.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Document(collection = "products")
@Data
public class Product {

    @Id
    private String id;

    private String name;

    private String content;

    private String description;

    private double price;

    private int stock_quantity;

    @DBRef
    private Product_Attributes product_attributes;

    @DBRef
    private Category category;

    private long watchCount;

    @DBRef
    private Brand brand;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date updatedAt;

    private String image;

    private List<String> images;
}
