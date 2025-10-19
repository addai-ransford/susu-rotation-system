package be.susu.db.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import be.susu.db.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
