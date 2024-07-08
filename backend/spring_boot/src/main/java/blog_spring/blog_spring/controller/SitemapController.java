package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.model.Product;
import blog_spring.blog_spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SitemapController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/sitemap.xml")
    public ResponseEntity<String> generateSitemap() {

        List<Product> products = productRepository.findAll();

        List<String> urls = new ArrayList<>();
        urls.add("http://localhost:3000"); // Trang chủ
        for (Product product : products) {
            urls.add("http://localhost:3000/products/" + product.getId());
        }

        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        for (String url : urls) {
            xmlBuilder.append("<url>\n");
            xmlBuilder.append("<loc>").append(url).append("</loc>\n");
            xmlBuilder.append("</url>\n");
        }
        xmlBuilder.append("</urlset>");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(xmlBuilder.toString());
    }
}
