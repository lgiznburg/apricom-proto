/**
 * @author Fedor Metelkin.
 *
 * Indroduces grid veiw with requests
 * */

package ru.apricom.testapp.pages.requests;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.ioc.annotations.Inject;
import ru.apricom.testapp.dao.RequestDao;
import ru.apricom.testapp.entities.catalogs.AdmissionType;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;

/**
 * @author leonid.
 */
@RequiresUser
public class List {
    static private final int MAX_RESULTS = 30;

    // Screen fields

    @Property
    private java.util.List<AdmissionType> listRows;

    // Generally useful bits and pieces

    @Inject
    private RequestDao requestDao;

    @InjectComponent
    private Grid grid;

    // The code

    void setupRender() {
        // Get all persons - ask business service to find them (from the database)
        listRows = requestDao.findAll(AdmissionType.class);

        if (grid.getSortModel().getSortConstraints().isEmpty()) {
            grid.getSortModel().updateSort("title");
        }
    }
}
