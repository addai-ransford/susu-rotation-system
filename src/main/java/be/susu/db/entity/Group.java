package be.susu.db.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @Column(nullable = false)
    private BigDecimal contributionAmount;

    private boolean active = true;
    private UUID currentBeneficiaryId;

    private int rotationIndex = -1;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    public enum Frequency {
        DAILY, WEEKLY, BIWEEKLY, MONTHLY
    }
}
