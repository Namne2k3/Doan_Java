package blog_spring.blog_spring.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "vouchers")
@Data
public class Voucher {
    public Voucher() {
        createdAt = new Date();
    }
    @Id
    private String id;

    private String userId;

    private int percent;

    private Date createdAt;
}
