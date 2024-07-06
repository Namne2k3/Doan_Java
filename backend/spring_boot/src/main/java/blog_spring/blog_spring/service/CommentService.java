package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqResComment;
import blog_spring.blog_spring.model.Comment;
import blog_spring.blog_spring.repository.CommentRepository;
import blog_spring.blog_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    public ReqResComment updateVerifyComment(String id, String verify) {
        ReqResComment reqRes = new ReqResComment();
        try {
            var comment = commentRepository.findById(id).get();
            if ( comment.getId() != null ) {
                if ( verify.equals("true") ) {
                    comment.setVerify(true);
                } else {
                    comment.setVerify(false);
                }
                var savedComment = commentRepository.save(comment);
                reqRes.setData(savedComment);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Đã kiểm duyệt phản hồi");
            } else {
                throw new Error("Phản hồi không tồn tại");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Có lỗi: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqResComment getVerifyComments (String verify) {
        ReqResComment reqRes = new ReqResComment();
        try {
            var listComment = commentRepository.findAll();
            if (verify.isEmpty()) {
                reqRes.setDataList(listComment);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Thành công");
            } else {
                if ( verify.equals("true") ) {
                    listComment = listComment.stream().filter(p -> p.isVerify()).toList();
                } else {
                    listComment = listComment.stream().filter(p -> !p.isVerify()).toList();
                }
                if (!listComment.isEmpty()) {
                    reqRes.setDataList(listComment);
                    reqRes.setStatusCode(200);
                    reqRes.setMessage("Thành công");
                } else {
                    reqRes.setStatusCode(404);
                    reqRes.setMessage("Chưa có bất kì đánh giá nào");
                }
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Có lỗi: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqResComment getComments() {
        ReqResComment reqRes = new ReqResComment();
        try {
            var listComment = commentRepository.findAll();
            if (!listComment.isEmpty()) {
                reqRes.setDataList(listComment);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Thành công");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Chưa có bất kì đánh giá nào");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Có lỗi: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqResComment getCommentsByProductId(String productId) {
        ReqResComment reqRes = new ReqResComment();

        try {
            var listComment = commentRepository.findAllByProductId(productId);
            if (!listComment.isEmpty()) {
                reqRes.setDataList(listComment);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Thành công");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Chưa có bất kì đánh giá nào cho sản phẩm này");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Có lỗi: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqResComment addComment(ReqResComment comment) {
        {
            ReqResComment reqRes = new ReqResComment();

            try {
                var newComment = new Comment();

                newComment.setProductId(comment.getProductId());

                var user = userRepository.findById(comment.getUser().getId()).get();
                if (user.getId() != null) {
                    newComment.setUser(user);

                } else {
                    throw new Exception("Tài khoản không tồn tại");
                }

                newComment.setText(comment.getText());
                newComment.setRate(comment.getRate());
                newComment.setImages(comment.getImages());
                var saved = commentRepository.save(newComment);


                if (saved.getId() != null) {
                    reqRes.setData(saved);
                    reqRes.setStatusCode(200);
                    reqRes.setMessage("Thành công");
                } else {
                    reqRes.setStatusCode(404);
                    reqRes.setMessage("Không thể đăng tải đánh giá. Vui lòng thử lại sau!");
                }

            } catch (Exception e) {
                reqRes.setStatusCode(500);
                reqRes.setMessage("Có lỗi: " + e.getMessage());
            }
            return reqRes;
        }
    }
}
