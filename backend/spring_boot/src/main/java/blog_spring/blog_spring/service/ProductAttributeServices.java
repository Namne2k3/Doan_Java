package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqRes;
import blog_spring.blog_spring.model.Product_Attributes;
import blog_spring.blog_spring.repository.ProductAttributesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductAttributeServices {

    @Autowired
    private ProductAttributesRepository repo;

    public ReqRes addProductAttributes(Product_Attributes attributes) {
        ReqRes resp = new ReqRes();

        try {
            Product_Attributes attr = new Product_Attributes();

            attr.setProduct(attributes.getProduct());
            attr.setGraphic(attributes.getGraphic().toString());
            attr.setCpu(attributes.getCpu());
            attr.setRam(attributes.getRam());
            attr.setSsd(attributes.getSsd());
            attr.setPanel(attributes.getPanel());
            attr.setScreen_size(attributes.getScreen_size());
            attr.setBrightness(attributes.getBrightness());
            attr.setResolution(attributes.getResolution());
            attr.setWeight(attributes.getWeight());


            var saved = repo.save(attr);

            if ( saved.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Saved product attributes Successful");
                resp.setData(saved);
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }
}
