package com.jinanging.codeblue.domain;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "scenario_flow")
public class ScenarioFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flow_id")
    private Integer flowId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_scenario_id")
    private Scenario currentScenario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_scenario_id")
    private Scenario nextScenario;

    @Column(name = "condition_type", length = 50)
    private String conditionType;

    @Column(name = "condition_value", length = 100)
    private String conditionValue;

    @Column(name = "choice_text", length = 255)
    private String choiceText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
