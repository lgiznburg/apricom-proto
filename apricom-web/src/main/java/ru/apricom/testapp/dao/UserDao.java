package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.auth.RolesNames;
import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.auth.UserRole;

public interface UserDao extends BaseDao {

    User findByUserName( String userName );

    String encrypt( String password );

    UserRole findRoleByName( RolesNames roleName );
}
