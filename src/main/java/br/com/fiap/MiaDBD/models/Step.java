package br.com.fiap.MiaDBD.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_MIA_MENSAGEM")
public class Step {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 3)
    private String description;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "taskId")
    private Task task;
}
