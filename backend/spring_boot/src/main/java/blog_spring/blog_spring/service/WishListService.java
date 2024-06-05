package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqResWishList;
import blog_spring.blog_spring.dto.ReqResWishList;
import blog_spring.blog_spring.model.Review;
import blog_spring.blog_spring.model.WishList;
import blog_spring.blog_spring.repository.ReviewRepository;
import blog_spring.blog_spring.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    public ReqResWishList getById(String id) {
        ReqResWishList resp = new ReqResWishList();

        try {
            WishList wishList = wishListRepository.findById(id).get();
            if ( wishList.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Get WishList Successful");
                resp.setData(wishList);
            } else {
                throw new Exception("WishList Not Found");
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResWishList getAllWishList() {
        ReqResWishList reqRes = new ReqResWishList();

        try {

            List<WishList> result = wishListRepository.findAll();

            if (!result.isEmpty()) {
                reqRes.setDataList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No WishLists found");
            }

        } catch (Exception e) {

            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());

        }
        return reqRes;
    }

    public ReqResWishList addWishList(ReqResWishList registrationRequest) {
        ReqResWishList resp = new ReqResWishList();

        try {
            WishList wishList = new WishList();
            wishList.setUser(registrationRequest.getUser());
            wishList.setProduct(registrationRequest.getProduct());
            wishList.setCreatedAt(new Date());

            var saved = wishListRepository.save(wishList);

            if ( saved.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Saved WishList Successful");
                resp.setData(saved);
            }  else {
                throw new Exception("Save WishList Failed");
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResWishList updateWishList(String id, ReqResWishList registrationRequest) {
        ReqResWishList resp = new ReqResWishList();

        try {
            WishList wishList = wishListRepository.findById(id).get();
            if ( wishList == null ) {
                throw new Exception("Not found WishList");
            }

            wishList.setUser(registrationRequest.getUser());
            wishList.setProduct(registrationRequest.getProduct());

            var saved = wishListRepository.save(wishList);

            if ( saved.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Updated Review Successful");
                resp.setData(saved);
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResWishList deleteWishList(String id) {
        ReqResWishList reqRes = new ReqResWishList();
        try {
            WishList wishList = wishListRepository.findById(id).get();
            if ( wishList != null ) {
                wishListRepository.deleteById(wishList.getId());
                reqRes.setStatusCode(200);
                reqRes.setMessage("WishList deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("WishList not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting wishList: " + e.getMessage());
        }
        return reqRes;
    }

}
