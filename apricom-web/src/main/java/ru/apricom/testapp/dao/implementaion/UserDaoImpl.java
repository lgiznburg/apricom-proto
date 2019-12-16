package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.UserDao;
import ru.apricom.testapp.entities.auth.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author leonid.
 */
public class UserDaoImpl extends BaseDaoImpl implements UserDao {
    @Override
    public User findByUserName( String username ) {
        Criteria criteria = session.createCriteria( User.class )
                .add( Restrictions.eq( "username", username ) );
        return (User) criteria.uniqueResult();
    }


    @Override
    public String encrypt( String password ) {
        try {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            md.update( password.getBytes() );
            BigInteger hash = new BigInteger( 1, md.digest() );
            StringBuilder passwordHash = new StringBuilder( hash.toString( 16 ) );
            while ( passwordHash.length() < 32 ) passwordHash.insert( 0, "0" );
            return passwordHash.toString();
        } catch ( NoSuchAlgorithmException e ) {
            throw new RuntimeException( e );
        }
    }

}
