package be.susu.db.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import be.susu.db.entity.Member;


public interface MemberRepository extends JpaRepository<Member, UUID> {
List<Member> findByGroupId(UUID groupId);
}