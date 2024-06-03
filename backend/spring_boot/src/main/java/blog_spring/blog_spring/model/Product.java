package blog_spring.blog_spring.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
    private Product_Attributes attributes;

    @DBRef
    private Category category;

    @DBRef
    private Brand brand;

    @DBRef
    private List<Order> orders ;

    private Date createdAt;

    private Date updatedAt;

    private String image;
}
