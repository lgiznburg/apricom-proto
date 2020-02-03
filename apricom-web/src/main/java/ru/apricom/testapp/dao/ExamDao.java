package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.exams.EntrantResult;
import ru.apricom.testapp.entities.exams.EntrantToExam;

import java.util.List;

/**
 * @author leonid.
 */
public interface ExamDao extends BaseDao {
    List<EntrantResult> findEntrantResults( Entrant entrant );

    List<EntrantToExam> findExamsForEntrant( Entrant entrant );
}
