package ru.apricom.testapp.entities.auth;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * @author leonid.
 *
 * User role
 *
 */
@Entity
@Table(name = "roles")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1977772580202380504L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String authority;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "role_permissions",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")}
    )
    @Fetch(FetchMode.SELECT )
    @Cascade(org.hibernate.annotations.CascadeType.ALL )
    private Set<GrantedPermission> permissions;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority( String authority ) {
        this.authority = authority;
    }

    public Set<GrantedPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions( Set<GrantedPermission> permissions ) {
        this.permissions = permissions;
    }

    @Transient
    public RolesNames getRoleName() {
        return RolesNames.valueOf( authority );
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof UserRole) ) return false;
        UserRole userRole = (UserRole) o;
        return id == userRole.id &&
                authority.equals( userRole.authority );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, authority );
    }
}
