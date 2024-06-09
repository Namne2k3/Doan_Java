package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqResBrand;
import blog_spring.blog_spring.dto.ReqResCart;
import blog_spring.blog_spring.dto.ReqResProduct;
import blog_spring.blog_spring.model.Brand;
import blog_spring.blog_spring.model.Cart;
import blog_spring.blog_spring.model.Product;
import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.repository.CartRepository;
import blog_spring.blog_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

//    public ReqResCart deleteById(String id) {
//        ReqResCart reqRes = new ReqResCart();
//        try {
//            Cart cart = cartRepository.findById(id).get();
//            if ( cart.getId() != null ) {
//                cartRepository.deleteAllById(Collections.singleton(cart.getId()));
//                reqRes.setStatusCode(200);
//                reqRes.setMessage("Cart deleted successfully");
//            } else {
//                reqRes.setStatusCode(404);
//                reqRes.setMessage("Cart not found for deletion");
//            }
//        } catch (Exception e) {
//            reqRes.setStatusCode(500);
//            reqRes.setMessage("Error occurred while deleting cart: " + e.getMessage());
//        }
//        return reqRes;
//    }

    public ReqResCart deleteById(String userId,String id) {
        ReqResCart reqRes = new ReqResCart();
        try {
            Optional<Cart> optionalCart = cartRepository.findById(id);
            if (optionalCart.isPresent()) {
                Cart cart = optionalCart.get();
                cartRepository.deleteById(id);

                // Cập nhật lại user sau khi xóa cart
                User user = userRepository.findById(userId).get();
                if (user.getId() != null) {
                    user.getCarts().remove(cart);
                    userRepository.save(user);
                }

                reqRes.setStatusCode(200);
                reqRes.setMessage("Cart deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Cart not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting cart: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqResCart updateCart(String id, Cart cart) {
        ReqResCart reqRes = new ReqResCart();
        try {
            Cart result = cartRepository.findById(id).get();
            if (result.getId() != null) {
                result.setQuantity(cart.getQuantity());

                var savedCart = cartRepository.save(result);

                reqRes.setData(savedCart);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Update cart success");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No carts found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqResCart getAllCart(String userId) {
        ReqResCart reqRes = new ReqResCart();
        try {
            List<Cart> result = userRepository.findById(userId).get().getCarts();
            if (!result.isEmpty()) {
                reqRes.setDataList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No carts found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqResCart addCart(String userId, Product product) {
        ReqResCart resp = new ReqResCart();
        try {
            User user = userRepository.findById(userId).get();
            if ( user.getCarts() == null ) {
                user.setCarts(new ArrayList<Cart>());
            }
            Optional<Cart> findCart = user.getCarts()
                    .stream()
                    .filter(cart ->
                            cart.getProduct().getId().equals(product.getId()))
                    .findFirst();
            if ( findCart.isPresent() ) {

                findCart.get().setQuantity(findCart.get().getQuantity()+1);
                var savedCart = cartRepository.save(findCart.get());
                resp.setStatusCode(200);
                resp.setMessage("Successful");
                resp.setData(savedCart);

            } else {
                if ( product.getId() != null && user.getId() != null ) {
                    Cart cart = new Cart();
                    cart.setProduct(product);
                    cart.setCreatedAt(new Date());
                    cart.setQuantity(1);

                    var savedCart = cartRepository.save(cart);
                    if ( savedCart.getId() != null ) {
                        resp.setStatusCode(200);
                        resp.setMessage("Successful");
                        resp.setData(savedCart);

                        user.getCarts().add(savedCart);
                        userRepository.save(user);
                    }
                    else {
                        resp.setStatusCode(404);
                        resp.setError("Can't save the cart to database");
                    }
                } else {
                    throw new Exception("Error occurred while adding product to Cart");
                }
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }


}
