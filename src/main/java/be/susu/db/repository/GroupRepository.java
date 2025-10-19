package be.susu.db.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.susu.db.entity.Group;

@Repository
public interface  GroupRepository extends JpaRepository<Group, UUID> {

}
