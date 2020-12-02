package mx.edu.uttt.Freion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String userPhotoUrl;
    private String name;
    private String username;
    private Instant date;
    private String contentType;
    private String content;
    private String privacy;
    private Long opinions;
    private Long comments;
    private Long views;
}
