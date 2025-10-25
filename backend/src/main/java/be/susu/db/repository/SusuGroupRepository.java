package be.susu.db.repository;

import be.susu.db.entity.SusuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SusuGroupRepository extends JpaRepository<SusuGroup, UUID> {

}
