package ru.apricom.testapp.services;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import ru.apricom.testapp.dao.UserDao;
import ru.apricom.testapp.entities.auth.GrantedPermission;
import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.auth.UserRole;

/**
 * @author leonid.
 */
public class UserDetailsRealm extends AuthorizingRealm {

    private final UserDao userDao;

    public UserDetailsRealm( UserDao userDao ) {
        super(new MemoryConstrainedCacheManager());
        this.userDao = userDao;
        setName("adminAccounts");
        setAuthenticationTokenClass(UsernamePasswordToken.class);
        setCredentialsMatcher(new HashedCredentialsMatcher( Md5Hash.ALGORITHM_NAME));
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo( PrincipalCollection principals ) {
        if (principals == null) throw new AuthorizationException("PrincipalCollection was null, which should not happen");

        if (principals.isEmpty()) return null;

        if (principals.fromRealm(getName()).size() <= 0) return null;

        String username = (String) principals.fromRealm(getName()).iterator().next();
        if (username == null) return null;
        User user = userDao.findByUserName( username );
        if (user == null) return null;
        SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();

        for ( UserRole role : user.getRoles()) {
            authInfo.addRole( role.getAuthority() );
            // add role permissions  TODO check AuthorizationRealm for role permission resolver
            for ( GrantedPermission permission : role.getPermissions() ) {
                authInfo.addStringPermission( permission.getPermission() );
            }
        }

        // add personal permissions
        for ( GrantedPermission permission : user.getPermissions() )
            authInfo.addStringPermission( permission.getPermission() );

        return authInfo;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token ) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        String username = upToken.getUsername();

        // Null username is invalid
        if (username == null) { throw new AccountException("Null user names are not allowed by this realm."); }

        User user = userDao.findByUserName( username );
        if ( user == null ) {
            return null;
        }

        if ( !user.isEnabled() ) { throw new LockedAccountException("Account [" + username + "] is locked."); }
        /*iif (user.isCredentialsExpired()) {
            String msg = "The credentials for account [" + username + "] are expired";
            throw new ExpiredCredentialsException(msg);
        }*/
        return new SimpleAuthenticationInfo(username, user.getPassword(), /*new SimpleByteSource(user.getPasswordSalt()),*/ getName());
    }
}
