package br.com.fiap.MiaDBD.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_MIA_APLICATIVO")
public class Application {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 50)
    private String name;

    @Size(min = 3, max = 50)
    private String description;

    @NotBlank(message = "O código figma é obrigatório ")
    @Size(min = 3, max = 50)
    private String figmaId;

    @JsonIgnore
    @OneToMany(mappedBy = "application")
    private Set<Task> tasks;

}
