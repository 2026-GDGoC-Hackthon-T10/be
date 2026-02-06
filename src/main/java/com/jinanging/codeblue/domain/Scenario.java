package com.jinanging.codeblue.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "scenario")
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scenario_id")
    private Integer scenarioId;

    @Column(nullable = false)
    private Integer stage;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String narration;

    @Column(name = "`type`", length = 50)
    private String type;

    @Column(name = "required_correct_count")
    private Integer requiredCorrectCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "currentScenario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScenarioFlow> outgoingFlows;

    @OneToMany(mappedBy = "nextScenario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScenarioFlow> incomingFlows;
}
