package mx.edu.uttt.Freion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @Email
    private String email;
    @NotBlank
    private String password;
}
