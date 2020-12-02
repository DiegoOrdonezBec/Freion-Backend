package mx.edu.uttt.Freion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @Column(nullable = false)
    private Instant date;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private ContentType contentType;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Privacy privacy;

    @ManyToMany(mappedBy = "posts", cascade = CascadeType.REFRESH)
    private Set<User> viewers;
}
