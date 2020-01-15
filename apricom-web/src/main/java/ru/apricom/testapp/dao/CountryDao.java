package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.catalogs.Country;

import java.util.List;

/**
 * @author leonid.
 */
public interface CountryDao extends BaseDao {
    List<String> findNames( String startsWith, int maxResults );

    Country findByName( String name );
}
