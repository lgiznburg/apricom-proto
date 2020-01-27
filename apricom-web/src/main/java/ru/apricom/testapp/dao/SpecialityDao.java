package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.base.EducationalProgram;

import java.util.List;

/**
 * @author leonid.
 */
public interface SpecialityDao extends BaseDao {
    List<EducationalProgram> findNotSelectedPrograms( List<EducationalProgram> programs );
}
