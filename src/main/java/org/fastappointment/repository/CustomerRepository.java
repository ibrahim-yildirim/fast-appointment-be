package org.fastappointment.repository;

import org.fastappointment.entity.concretes.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUserId(Long userId);

    Optional<Customer> findByBusinessIdAndPhone(Long businessId, String phone);

    boolean existsByBusinessIdAndPhone(Long businessId, String phone);

    @Query("select c from Customer c left join c.business biz where biz.id = :businessId or c.id in (select b.customer.id from Booking b where b.business.id = :businessId)")
    List<Customer> findAllOfBusiness(@Param("businessId") Long businessId);

    @Query("select c from Customer c left join c.business biz where c.id = :id and (biz.id = :businessId or c.id in (select b.customer.id from Booking b where b.business.id = :businessId))")
    Optional<Customer> findByIdOfBusiness(@Param("id") Long id, @Param("businessId") Long businessId);
}
