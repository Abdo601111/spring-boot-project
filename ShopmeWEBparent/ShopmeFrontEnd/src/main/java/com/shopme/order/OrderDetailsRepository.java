package com.shopme.order;

import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail,Integer> {


    @Query("SELECT COUNT(d) FROM OrderDetail d JOIN OrderTrack t ON d.orderDetails.id=t.order.id " +
            "WHERE d.product.id=?1 AND d.orderDetails.customer.id=?2 AND " +
            "t.status=?3")
    public  long  countByProductAndCustomerAndOrderStatus(Integer productId,
                     Integer customerId, OrderStatus orderStatus);

//    @Query("SELECT COUNT(d) ")
//    List<OrderDetail> getTop5ProductSale();

}
