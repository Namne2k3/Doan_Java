package blog_spring.blog_spring.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "product_attributes")
public class Product_Attributes {

    @Id
    private String id;

    @DBRef
    private Product product;

    // Common attributes
    private String screen_size;
    private String resolution;
    private double weight;
    private int ram;

    // Laptop specific attributes
    private String graphic;
    private String cpu;
    private Integer ssd;
    private String panel;
    private Integer brightness;

    // Mobile specific attributes
    private String chipset;
    private String gpu;
    private String battery;
    private String back_camera;
    private String front_camera;
    private String video_feature_back;
    private String video_feature_front;
    private String video_record;
    private String charge_tech;
    private String SIM;

    // Watch specific attributes
    private String screen_tech;
    private String diameter;
    private String design;
    private double time_charge;
    private String battery_life;
}