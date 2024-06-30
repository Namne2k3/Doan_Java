package blog_spring.blog_spring.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        return "spring_blog";
    }
    @Override
    public MongoClient mongoClient() {
//        return MongoClients.create("mongodb+srv://nhpn2003:namproplayer2003@cluster0.hhzyw7c.mongodb.net/");
        return MongoClients.create(mongoUri);
    }
//
//    @Override
//    protected Collection<String> getMappingBasePackages() {
//        return Collections.singleton("blog_spring.blog_spring.config");
//    }
}
