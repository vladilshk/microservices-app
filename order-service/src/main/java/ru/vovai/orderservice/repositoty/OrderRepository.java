package ru.vovai.orderservice.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vovai.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
