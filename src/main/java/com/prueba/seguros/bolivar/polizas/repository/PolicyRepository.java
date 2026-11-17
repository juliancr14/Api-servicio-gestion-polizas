package com.prueba.seguros.bolivar.polizas.repository;

import com.prueba.seguros.bolivar.polizas.model.Policy;
import com.prueba.seguros.bolivar.polizas.model.PolicyStatus;
import com.prueba.seguros.bolivar.polizas.model.PolicyType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

	@Query("""
			select p from Policy p
			where (:type is null or p.type = :type)
			and (:status is null or p.status = :status)
			""")
	List<Policy> findByTypeAndStatus(@Param("type") PolicyType type, @Param("status") PolicyStatus status);
}
