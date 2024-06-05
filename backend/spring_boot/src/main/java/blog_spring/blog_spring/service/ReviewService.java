package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqResBrand;
import blog_spring.blog_spring.dto.ReqResReview;
import blog_spring.blog_spring.model.Brand;
import blog_spring.blog_spring.model.Review;
import blog_spring.blog_spring.repository.BrandRepository;
import blog_spring.blog_spring.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public ReqResReview getById(String id) {
        ReqResReview resp = new ReqResReview();

        try {
            Review review = reviewRepository.findById(id).get();
            if ( review.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Get Review Successful");
                resp.setData(review);
            } else {
                throw new Exception("Review Not Found");
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResReview getAllReviews() {
        ReqResReview reqRes = new ReqResReview();

        try {
            List<Review> result = reviewRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setDataList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No Reviews found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqResReview addReview(ReqResReview registrationRequest) {
        ReqResReview resp = new ReqResReview();

        try {
            Review review = new Review();
            review.setProduct(registrationRequest.getProduct());
            review.setUser(registrationRequest.getUser());
            review.setRating(registrationRequest.getRating());
            review.setComment(registrationRequest.getComment());
            review.setCreatedAt(new Date());
            review.setUpdatedAt(new Date());

            var saved = reviewRepository.save(review);

            if ( saved.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Saved Review Successful");
                resp.setData(saved);
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResReview updateReview(String id, ReqResReview registrationRequest) {
        ReqResReview resp = new ReqResReview();

        try {
            Review review = reviewRepository.findById(id).get();
            if ( review != null ) {
                review.setProduct(registrationRequest.getProduct());
                review.setUser(registrationRequest.getUser());
                review.setRating(registrationRequest.getRating());
                review.setComment(registrationRequest.getComment());
                review.setUpdatedAt(new Date());
            } else {
               throw new Exception("Not found Review");
            }

            var saved = reviewRepository.save(review);

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

    public ReqResReview deleteReview(String id) {
        ReqResReview reqRes = new ReqResReview();
        try {
            Review review = reviewRepository.findById(id).get();
            if ( review != null ) {
                reviewRepository.deleteById(review.getId());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Review deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Review not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting review: " + e.getMessage());
        }
        return reqRes;
    }
}
