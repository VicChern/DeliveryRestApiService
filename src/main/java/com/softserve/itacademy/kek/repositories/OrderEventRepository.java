package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {

    OrderEvent findByGuid(UUID guid);
}
