package be.susu.db.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import be.susu.db.entity.Membership;
import be.susu.db.entity.SusuGroup;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {

    List<Membership> findByGroupOrderByJoinOrder(SusuGroup group);
    List<Membership> findByGroupIdOrderByJoinOrderAsc(UUID groupId);


    Optional<Membership> findByGroupAndKeycloakUserId(SusuGroup group, String keycloakUserId);

    @Query("SELECT MAX(m.joinOrder) FROM Membership m WHERE m.group = :group")
    Integer findMaxJoinOrder(@Param("group") SusuGroup group);
}
