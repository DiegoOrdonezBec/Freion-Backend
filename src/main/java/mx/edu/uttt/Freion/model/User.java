package mx.edu.uttt.Freion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profilePhotoUrl;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="follow"
            ,joinColumns = @JoinColumn(name = "follower_id")
            ,inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    private Set<User> follows;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="block"
            ,joinColumns = @JoinColumn(name = "blocker_id")
            ,inverseJoinColumns = @JoinColumn(name = "blocked_id")
    )
    private Set<User> blocks;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="member"
            ,joinColumns = @JoinColumn(name = "user_id")
            ,inverseJoinColumns = @JoinColumn(name = "conversation_id")
    )
    private Set<Conversation> conversations;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="view"
            ,joinColumns = @JoinColumn(name = "user_id")
            ,inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> posts;
}
