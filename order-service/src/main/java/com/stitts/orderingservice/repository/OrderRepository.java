package com.stitts.orderingservice.repository;

import com.stitts.orderingservice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
