package blog_spring.blog_spring.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Mobile_Attributes {
    @Id
    private String id;

    @DBRef
    private Product product;

    private String screen_size;

    private String resolution;

    private String back_camera;

    private String front_camera;

    private String video_feature_back;

    private String video_feature_front;

    private String video_record;

    private String chipset;

    private String cpu;

    private String gpu;

    private String battery;

    private String charge_tech;

    private double weight;

}
