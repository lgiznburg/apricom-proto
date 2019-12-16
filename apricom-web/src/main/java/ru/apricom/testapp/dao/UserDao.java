package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.auth.User;

public interface UserDao extends BaseDao {

    User findByUserName( String userName );

    String encrypt( String password );
}
