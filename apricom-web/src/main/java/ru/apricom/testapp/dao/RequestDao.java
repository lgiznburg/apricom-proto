/**
 * @author Fedor Metelkin.
 *
 * Indroduces basic request object
 * */

package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.catalogs.AdmissionType;

public interface RequestDao extends BaseDao {
    AdmissionType findByTitle(String title);
}
