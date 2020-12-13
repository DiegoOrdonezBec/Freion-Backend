package mx.edu.uttt.Freion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Date lastMessage;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="member"
            ,joinColumns = @JoinColumn(name = "conversation_id")
            ,inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;
}
