package ru.apricom.testapp.entities.auth;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.annotation.RegEx;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author leonid.
 *
 * Permissions could be granted through role or directly to the user
 */
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 7796037943422850089L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    @Column(unique = true)
    @Email
    @NotBlank
    private String username;

    @Column
    private String password;

    @Column
    @NotBlank
    @Pattern( regexp = "[А-Яа-я0-9 ]*")
    private String firstName;

    @Column
    private String middleName;

    @Column
    @NotBlank
    @Pattern( regexp = "[А-Яа-я0-9 ]*")
    private String lastName;

    @Column
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @Fetch(FetchMode.SELECT )
    @Cascade(CascadeType.ALL )
    private List<UserRole> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_permissions",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")}
    )
    @Fetch(FetchMode.SELECT )
    @Cascade(CascadeType.ALL )
    private List<GrantedPermission> permissions;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName( String middleName ) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles( List<UserRole> roles ) {
        this.roles = roles;
    }

    public List<GrantedPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions( List<GrantedPermission> permissions ) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof User) ) return false;
        User user = (User) o;
        return id == user.id &&
                username.equals( user.username );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, username );
    }
}
