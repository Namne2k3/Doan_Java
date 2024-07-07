package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqRes;
import blog_spring.blog_spring.model.Order;
import blog_spring.blog_spring.model.Voucher;
import blog_spring.blog_spring.repository.OrderRepository;
import blog_spring.blog_spring.repository.UserRepository;
import blog_spring.blog_spring.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public ReqRes calculateVoucher (String userId, String voucherPercent) {
        ReqRes reqRes = new ReqRes();
        try {

            var findUser = userRepository.findById(userId).get();

            if ( findUser.getId() == null ) {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Tài khoản không tồn tại");
                return reqRes;
            }


            // > 10tr
            if (voucherPercent.equals("10")) {
                Voucher voucher = new Voucher();
                voucher.setUserId(userId);
                voucher.setPercent(10);

                var savedVoucher = voucherRepository.save(voucher);

                findUser.setAmount(findUser.getAmount() - 10000000);
                userRepository.save(findUser);

                reqRes.setData(savedVoucher);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Đã áp dụng voucher " + savedVoucher.getPercent() + "%");
            }

            // > 20tr
            if (voucherPercent.equals("15")) {
                Voucher voucher = new Voucher();
                voucher.setUserId(userId);
                voucher.setPercent(15);

                var savedVoucher = voucherRepository.save(voucher);

                findUser.setAmount(findUser.getAmount() - 20000000);
                userRepository.save(findUser);

                reqRes.setData(savedVoucher);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Đã áp dụng voucher " + savedVoucher.getPercent() + "%");
            }

            // > 50tr
            if (voucherPercent.equals("25")) {
                Voucher voucher = new Voucher();
                voucher.setUserId(userId);
                voucher.setPercent(25);

                var savedVoucher = voucherRepository.save(voucher);

                findUser.setAmount(findUser.getAmount() - 50000000);
                userRepository.save(findUser);

                reqRes.setData(savedVoucher);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Đã áp dụng voucher " + savedVoucher.getPercent() + "%");
            }



        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Có lỗi: " + e.getMessage());
        }
        return reqRes;
    }
}
