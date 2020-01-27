package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.SpecialityDao;
import ru.apricom.testapp.entities.base.EducationalProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author leonid.
 */
@SuppressWarnings( "unchecked" )
public class SpecialityDaoImpl extends BaseDaoImpl implements SpecialityDao {
    @Override
    public List<EducationalProgram> findNotSelectedPrograms( List<EducationalProgram> programs ) {

        Criteria criteria = session.createCriteria( EducationalProgram.class );
        if ( programs != null && programs.size() > 0 ) {
            List<Long> programsId = programs.stream().map( EducationalProgram::getId ).collect( Collectors.toList() );
            criteria.add( Restrictions.not( Restrictions.in( "id", programsId ) ) );
        }
        return criteria.list();
    }
}
