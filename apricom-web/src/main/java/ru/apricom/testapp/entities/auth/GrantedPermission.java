package ru.apricom.testapp.entities.auth;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author leonid.
 *
 * Permission is a main way to access application features
 *
 * This class represents specific permission grated to an user or to a role
 * Also we going to create Permission class which will represent permission hierarсhy in the system.
 * It will be used to assign permission interface.
 */
@Entity
@Table( name = "granted_permissions")
public class GrantedPermission implements Serializable {
    private static final long serialVersionUID = -2411848496533544192L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    @Column(unique = true)
    private String permission = "";

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission( String permission ) {
        this.permission = permission;
    }

    public GrantedPermission() {
    }

    public GrantedPermission( String permission ) {
        this.permission = permission;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof GrantedPermission) ) return false;
        GrantedPermission that = (GrantedPermission) o;
        return id == that.id &&
                permission.equals( that.permission );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, permission );
    }
}
