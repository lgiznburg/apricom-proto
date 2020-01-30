package ru.apricom.testapp.dao.implementaion;

import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.SortConstraint;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.EntrantDao;
import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.person.SpecialState;

import java.util.List;

/**
 * @author leonid.
 */
public class EntrantDaoImpl extends BaseDaoImpl implements EntrantDao {
    @Override
    public Entrant findByUser( User user ) {
        Criteria criteria = session.createCriteria( Entrant.class )
                .add( Restrictions.eq( "user", user ) )
                .setMaxResults( 1 );
        return (Entrant) criteria.uniqueResult();
    }

    @Override
    public SpecialState findSpecialState(Entrant entrant) {
        Criteria criteria = session.createCriteria( SpecialState.class )
                .add( Restrictions.eq( "entrant", entrant ) )
                .setMaxResults( 1 );
        return (SpecialState) criteria.uniqueResult();
    }

    @Override
    public long countEntrants(String filterLastName, String filterFistName, String filterMiddleName) {
        Criteria criteria = session.createCriteria(Entrant.class)
                .createAlias("personInfo", "personInfo")
                .createAlias("personInfo.name", "personName");
        if(filterLastName!=null && !filterLastName.isEmpty()){
            criteria.add(Restrictions.like("personName.surname", filterLastName+"%"));
        }
        if(filterFistName!=null && !filterFistName.isEmpty()){
            criteria.add(Restrictions.like("personName.firstName", filterFistName+"%"));
        }
        if(filterMiddleName!=null && !filterMiddleName.isEmpty()){
            criteria.add(Restrictions.like("personName.patronymic", filterMiddleName+"%"));
        }
        criteria.setProjection(Projections.rowCount());
        return (long) criteria.uniqueResult();
        //return 0;
    }

    @Override
    public List<Entrant> findByFilter(String filterLastName, String filterFistName, String filterMiddleName, int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
        Criteria criteria = session.createCriteria(Entrant.class)
                .createAlias("personInfo", "personInfo")
                .createAlias("personInfo.name", "personName");
        if(filterLastName!=null && !filterLastName.isEmpty()){
            criteria.add(Restrictions.like("personName.surname", filterLastName+"%"));
        }
        if(filterFistName!=null && !filterFistName.isEmpty()){
            criteria.add(Restrictions.like("personName.firstName", filterFistName+"%"));
        }
        if(filterMiddleName!=null && !filterMiddleName.isEmpty()){
            criteria.add(Restrictions.like("personName.patronymic", filterMiddleName+"%"));
        }
        for( SortConstraint sortConstraint : sortConstraints ) {
            if (!sortConstraint.getPropertyModel().getPropertyName().equalsIgnoreCase("fullName")) {
                criteria.addOrder(sortConstraint.getColumnSort()== ColumnSort.ASCENDING ?
                        Order.asc(sortConstraint.getPropertyModel().getPropertyName()) :
                        Order.desc(sortConstraint.getPropertyModel().getPropertyName()));
            }
            else {
                if(sortConstraint.getColumnSort()== ColumnSort.ASCENDING){
                    criteria.addOrder(Order.asc("personName.surname"))
                            .addOrder(Order.asc("personName.firstName"))
                            .addOrder(Order.asc("personName.patronymic"));
                }
                else {
                    criteria.addOrder(Order.desc("personName.surname"))
                            .addOrder(Order.desc("personName.firstName"))
                            .addOrder(Order.desc("personName.patronymic"));
                }
            }
        }
        criteria.setFirstResult(startIndex);
        criteria.setMaxResults(endIndex-startIndex + 1);
        return criteria.list();
    }
}
