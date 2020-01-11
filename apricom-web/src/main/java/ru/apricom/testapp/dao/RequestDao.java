/**
 * @author Fedor Metelkin.
 *
 * Indroduces basic request object
 * */

package ru.apricom.testapp.dao;

import org.apache.tapestry5.grid.SortConstraint;
import ru.apricom.testapp.entities.catalogs.AdmissionType;

import java.util.List;

public interface RequestDao extends BaseDao {
    java.util.List<AdmissionType> findByTitle(String title, int startIndex, int endIndex, List<SortConstraint> sortConstraints);

    long countAdmissionType(String title);
}
