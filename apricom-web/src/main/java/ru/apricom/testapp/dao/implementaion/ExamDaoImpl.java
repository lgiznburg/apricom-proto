package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.ExamDao;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.exams.EntrantResult;
import ru.apricom.testapp.entities.exams.EntrantToExam;

import java.util.List;

/**
 * @author leonid.
 */
@SuppressWarnings( "unchecked" )
public class ExamDaoImpl extends BaseDaoImpl implements ExamDao {
    @Override
    public List<EntrantResult> findEntrantResults( Entrant entrant ) {
        Criteria criteria = session.createCriteria( EntrantResult.class )
                .add( Restrictions.eq( "entrant", entrant ) );
        return criteria.list();
    }

    @Override
    public List<EntrantToExam> findExamsForEntrant( Entrant entrant ) {
        Criteria criteria = session.createCriteria( EntrantToExam.class )
                .add( Restrictions.eq( "entrant", entrant ) );
        return criteria.list();
    }
}
