package com.irms.table.repository;

import com.irms.table.domain.RestaurantTable;
import com.irms.table.domain.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, UUID> {

    List<RestaurantTable> findByStatus(TableStatus status);

    List<RestaurantTable> findByStatusAndCapacityGreaterThanEqual(TableStatus status, Integer minCapacity);

    List<RestaurantTable> findByCapacityGreaterThanEqual(Integer minCapacity);

    boolean existsByTableNumber(String tableNumber);
}
