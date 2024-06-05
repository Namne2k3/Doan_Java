package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqResOrder;
import blog_spring.blog_spring.model.Order;
import blog_spring.blog_spring.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

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

    public ReqResOrder addOrder(ReqResOrder registrationRequest) {
        ReqResOrder resp = new ReqResOrder();

        try {
            Order order = new Order();
            order.setUser(registrationRequest.getUser());
            order.setOrderDate(registrationRequest.getOrderDate());
            order.setTotalAmount(registrationRequest.getTotalAmount());
            order.setStatus(registrationRequest.getStatus());
            order.setShippingAddress(registrationRequest.getShippingAddress());
            order.setCreatedAt(new Date());
            order.setUpdatedAt(new Date());

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

    public ReqResOrder updateOrder(String id, ReqResOrder registrationRequest) {
        ReqResOrder resp = new ReqResOrder();

        try {
            Order order = orderRepository.findById(id).get();

            if ( order == null ) {
                throw new Exception("Order not found");
            }

            order.setUser(registrationRequest.getUser());
            order.setOrderDate(registrationRequest.getOrderDate());
            order.setTotalAmount(registrationRequest.getTotalAmount());
            order.setStatus(registrationRequest.getStatus());
            order.setShippingAddress(registrationRequest.getShippingAddress());
            order.setUpdatedAt(new Date());
            var saved = orderRepository.save(order);

            if ( saved.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Updated Order Successful");
                resp.setData(saved);
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
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
