/**
 * @author Fedor Metelkin.
 *
 * Indroduces basic request object
 * */

package ru.apricom.testapp.dao.implementaion;

import ru.apricom.testapp.dao.RequestDao;
import ru.apricom.testapp.entities.catalogs.AdmissionType;

public class RequestDaoImpl extends BaseDaoImpl implements RequestDao {

    @Override
    public AdmissionType findByTitle(String title) {
        return null;
    }
}
