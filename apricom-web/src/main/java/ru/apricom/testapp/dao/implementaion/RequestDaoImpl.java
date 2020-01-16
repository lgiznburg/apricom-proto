/**
 * @author Fedor Metelkin.
 *
 * Indroduces basic request object
 * */

package ru.apricom.testapp.dao.implementaion;

import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.SortConstraint;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.RequestDao;
import ru.apricom.testapp.entities.catalogs.AdmissionType;

import java.util.List;

public class RequestDaoImpl extends BaseDaoImpl implements RequestDao {

    @Override
    public java.util.List<AdmissionType> findByTitle(String title, int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
        java.util.List<AdmissionType> filteredList = new java.util.LinkedList<>();
        Criteria criteria = session.createCriteria(AdmissionType.class);
        if ( title != null && !title.isEmpty() ) {
            criteria.add(Restrictions.eq("title", title ));

        }
        for( SortConstraint sortConstraint : sortConstraints ) {
            criteria.addOrder(sortConstraint.getColumnSort()== ColumnSort.ASCENDING ?
                    Order.asc(sortConstraint.getPropertyModel().getPropertyName()) :
                    Order.desc(sortConstraint.getPropertyModel().getPropertyName()));
        }
        criteria.setFirstResult(startIndex);
        criteria.setMaxResults(endIndex-startIndex + 1);
        return criteria.list();
    }

    @Override
    public long countAdmissionType(String title) {
        Criteria criteria = session.createCriteria(AdmissionType.class);
            if ( title != null && !title.isEmpty() ) {
                criteria.add(Restrictions.eq("title", title ));
            }
            criteria.setProjection(Projections.rowCount());
        return (long) criteria.uniqueResult();
    }
}
