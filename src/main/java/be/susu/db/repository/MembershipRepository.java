package be.susu.db.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import be.susu.db.entity.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    List<Membership> findByGroupIdOrderByJoinOrder(UUID groupId);
    Optional<Membership> findByGroupIdAndKeycloakUserId(UUID groupId, String keycloakUserId);

    @Query("SELECT COALESCE(MAX(m.joinOrder),0) FROM Membership m WHERE m.group.id = :groupId")
    Integer findMaxJoinOrder(java.util.UUID groupId);
}
