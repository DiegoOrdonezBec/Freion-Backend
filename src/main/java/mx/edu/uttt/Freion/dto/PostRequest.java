package mx.edu.uttt.Freion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    @NotBlank
    private String contentType;
    @NotBlank
    private String content;
    @NotBlank
    private String privacy;
}
