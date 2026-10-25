package org.fastappointment.repository;

import jakarta.persistence.LockModeType;
import org.fastappointment.entity.concretes.Staff;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByBusinessIdAndActiveTrue(Long businessId);

    @EntityGraph(attributePaths = "services")
    List<Staff> findByBusinessIdOrderByIdAsc(Long businessId);

    List<Staff> findByBusinessIdAndIdIn(Long businessId, Collection<Long> ids);

    Optional<Staff> findByIdAndBusinessId(Long id, Long businessId);

    Optional<Staff> findByIdAndBusinessIdAndActiveTrue(Long id, Long businessId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Staff s where s.id = :id")
    Optional<Staff> lockById(@Param("id") Long id);
}
