package it.cgmconsulting.myblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name= "_user")
public class User extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore // non restituisci l'attributo nella repos
    @Column(nullable = false)
    private String password;

    @Column(length = 50)
    private String firstname;

    @Column(length = 50)
    private String latname;

    private String bio;

    private boolean enable = false;

    @ManyToMany
    @JoinTable(name="user_authority",
        joinColumns = @JoinColumn(name = "use_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "preferred_post",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    public Set<Post> preferredPosts = new HashSet<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
