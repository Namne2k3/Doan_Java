package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqResOrder;
import blog_spring.blog_spring.dto.ReqResOrderSearch;
import blog_spring.blog_spring.model.Order;
import blog_spring.blog_spring.model.OrderDetail;
import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.repository.OrderDetailRepository;
import blog_spring.blog_spring.repository.OrderRepository;
import blog_spring.blog_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder() {
        return new Order();
    }

    private LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public ReqResOrderSearch searchOrders(ReqResOrderSearch reqResOrderSearch) {
        ReqResOrderSearch resp = new ReqResOrderSearch();
        YearMonth yearMonth = null;
        LocalDate day = null;

        try {
            // Chuyển đổi year_month thành YearMonth nếu không null hoặc rỗng
            if (reqResOrderSearch.getYear_month() != null && !reqResOrderSearch.getYear_month().isBlank()) {
                yearMonth = YearMonth.parse(reqResOrderSearch.getYear_month(), DateTimeFormatter.ofPattern("yyyy-MM"));
            }

            // Chuyển đổi day thành LocalDate nếu không null hoặc rỗng
            if (reqResOrderSearch.getDay() != null && !reqResOrderSearch.getDay().isBlank()) {
                day = LocalDate.parse(reqResOrderSearch.getDay(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

            // Lấy tất cả các đơn hàng
            List<Order> listFinded = orderRepository.findAll();

            // Lọc danh sách dựa trên các điều kiện
            YearMonth finalYearMonth = yearMonth;
            LocalDate finalDay = day;
            listFinded = listFinded.stream()
                    .filter(p -> {
                        // Kiểm tra orderId
                        if (reqResOrderSearch.getSearch() != null && !reqResOrderSearch.getSearch().isEmpty()) {
                            if (!p.getId().equals(reqResOrderSearch.getSearch())) {
                                return false;
                            }
                        }

                        // Kiểm tra status
                        if (reqResOrderSearch.getStatus() != null && !reqResOrderSearch.getStatus().isEmpty()) {
                            if (!p.getStatus().equalsIgnoreCase(reqResOrderSearch.getStatus())) {
                                return false;
                            }
                        }

                        // Kiểm tra year_month
                        if (finalYearMonth != null) {
                            YearMonth orderYearMonth = YearMonth.from(convertDateToLocalDate(p.getOrderDate())); // Chuyển đổi orderDate thành YearMonth
                            if (!orderYearMonth.equals(finalYearMonth)) {
                                return false;
                            }
                        }

                        // Kiểm tra day
                        if (finalDay != null) {
                            LocalDate orderDate = convertDateToLocalDate(p.getOrderDate()); // Chuyển đổi Date thành LocalDate
                            if (!orderDate.equals(finalDay)) {
                                return false;
                            }
                        }

                        // Nếu tất cả điều kiện đều thỏa mãn
                        return true;
                    })
                    .collect(Collectors.toList());

            // Kiểm tra kết quả lọc và thiết lập phản hồi
            if (!listFinded.isEmpty()) {
                resp.setStatusCode(200);
                resp.setMessage("Get orders filter successfully");
                resp.setDataList(listFinded);
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Not Found");
            }

        } catch (Exception e) {
            // Xử lý ngoại lệ và thiết lập phản hồi lỗi
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        } finally {
            return resp;
        }
    }

    public ReqResOrder getMyOrders (String userId) {
        ReqResOrder resp = new ReqResOrder();
        try {
            var findUser = userRepository.findById(userId).get();
            if ( findUser.getId() != null ) {
                Sort sortByCreatedAtDesc = Sort.by(Sort.Direction.DESC, "createdAt");
                var findOrderList = orderRepository.findByUserId(findUser.getId(), sortByCreatedAtDesc);
                resp.setStatusCode(200);
                resp.setMessage("Get my orders successfully");
                resp.setDataList(findOrderList);
            }
            else {
                throw new Exception("User not found");
            }
        } catch (Exception e ) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResOrder getById(String id) {
        ReqResOrder resp = new ReqResOrder();

        try {
            Order order = orderRepository.findById(id).get();

            if ( order.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Get Order Successful");
                resp.setData(order);
            } else {
                throw new Exception("Order not found");
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResOrder getAllOrders() {
        ReqResOrder reqRes = new ReqResOrder();

        try {
            List<Order> result = orderRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setDataList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No brands found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqResOrder addOrder(Order registrationRequest) {
        ReqResOrder resp = new ReqResOrder();

        try {
            Order order = new Order();
            if ( registrationRequest.getUser() != null ) {
                var id = registrationRequest.getUser().getId();
                var findUser = userRepository.findById(id).get();
                order.setUser(findUser);
            }
            order.setOrderDate(new Date());
            order.setTotalAmount(registrationRequest.getTotalAmount());
            order.setStatus(registrationRequest.getStatus());
            order.setShippingAddress(registrationRequest.getShippingAddress());
            order.setEmail(registrationRequest.getEmail());
            order.setCreatedAt(new Date());
            order.setUpdatedAt(new Date());
            order.setPhone(registrationRequest.getPhone());


            var savedOrderDetails = orderDetailRepository.saveAll(registrationRequest.getDetails());

            order.setDetails(savedOrderDetails);

            var saved = orderRepository.save(order);

            if ( saved.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Saved Order Successful");
                resp.setData(saved);
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

//    public ReqResOrder updateOrder(String id, String status) {
//        ReqResOrder resp = new ReqResOrder();
//
//        try {
//            Order order = orderRepository.findById(id).get();
//
//            if ( order == null ) {
//                throw new Exception("Order not found");
//            }
//
////            order.setUser(registrationRequest.getUser());
////            order.setOrderDate(registrationRequest.getOrderDate());
////            order.setTotalAmount(registrationRequest.getTotalAmount());
////            order.setStatus(registrationRequest.getStatus());
////            order.setShippingAddress(registrationRequest.getShippingAddress());
//            order.setStatus(status);
//            order.setUpdatedAt(new Date());
//            var saved = orderRepository.save(order);
//
//            if ( saved.getId() != null ) {
//                resp.setStatusCode(200);
//                resp.setMessage("Updated Order Successful");
//                resp.setData(saved);
//            }
//
//        } catch(Exception e) {
//            resp.setStatusCode(500);
//            resp.setError(e.getMessage());
//        }
//        return resp;
//    }

    public ReqResOrder updateOrder(String id, String status) {
        ReqResOrder resp = new ReqResOrder();

        try {
            // Kiểm tra xem Order có tồn tại không
            Optional<Order> optionalOrder = orderRepository.findById(id);

            if (!optionalOrder.isPresent()) {
                resp.setStatusCode(404);
                resp.setMessage("Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp).getBody();
            }

            Order order = optionalOrder.get();
            order.setStatus(status);
            order.setUpdatedAt(new Date());

            // Lưu Order đã cập nhật
            Order savedOrder = orderRepository.save(order);

            resp.setStatusCode(200);
            resp.setMessage("Updated Order Successfully");
            resp.setData(savedOrder);

            return ResponseEntity.ok(resp).getBody();
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp).getBody();
        }
    }

    public ReqResOrder deleteOrder(String id) {
        ReqResOrder reqRes = new ReqResOrder();
        try {
            Order order = orderRepository.findById(id).get();
            if ( order == null ) {
                throw new Exception("Order not found");
            }
            orderRepository.deleteById(order.getId());
            reqRes.setStatusCode(200);
            reqRes.setMessage("Order deleted successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting order: " + e.getMessage());
        }
        return reqRes;
    }
}
