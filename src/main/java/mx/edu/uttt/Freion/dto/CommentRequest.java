package mx.edu.uttt.Freion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    @NotBlank
    private Long postId;
    @NotBlank
    private String contentType;
    @NotBlank
    private String content;
}
