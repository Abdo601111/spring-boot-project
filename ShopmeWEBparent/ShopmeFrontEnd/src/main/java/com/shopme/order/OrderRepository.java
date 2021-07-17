package com.shopme.order;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Order;

public interface OrderRepository  extends CrudRepository<Order, Integer>{

}
