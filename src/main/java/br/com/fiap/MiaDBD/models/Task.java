package br.com.fiap.MiaDBD.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_MIA_CONVERSA")
public class Task {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 50)
    private String title;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "applicationId")
    private Application application;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private List<Step> steps;
}
