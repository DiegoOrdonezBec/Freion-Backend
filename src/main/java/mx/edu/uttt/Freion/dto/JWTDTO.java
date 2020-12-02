package mx.edu.uttt.Freion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JWTDTO {
    @NotBlank
    private String token;
    @NotBlank
    private String username;
}
