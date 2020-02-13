/**
 * @author Fedor Metelkin.
 *
 * Indroduces grid veiw with requests
 * */

package ru.apricom.testapp.pages.requests;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import ru.apricom.testapp.auxilary.EntrantFacade;
import ru.apricom.testapp.dao.EntrantDao;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;
import ru.apricom.testapp.entities.entrant.Entrant;

import java.util.ArrayList;

/**
 * @author leonid.
 */
@RequiresUser
@Import (stylesheet = "context:static/css/requestList.css")
public class RequestList<E> {
    static private final int MAX_RESULTS = 30;

    // Screen fields

    @Property
    private java.util.List<String> titles;

    @Property
    @ActivationRequestParameter(value = "filterLastName")
    private String filterLastName;

    @Property
    @ActivationRequestParameter(value = "filterFirstName")
    private String filterFirstName;

    @Property
    @ActivationRequestParameter(value = "filterMiddleName")
    private String filterMiddleName;

    @Property
    @ActivationRequestParameter(value = "filterCaseNumber")
    private String filterCaseNumber;

    @Property
    @ActivationRequestParameter(value = "forChangeNumber")
    private int forChangeNumber;

    @Property
    private int numberOfEntrant=1;

    @Property
    private int[] numberOfEntrantList = {1, 30, 50, 100};

    @Property
    private EntrantFacade entrant;

    @Inject
    private EntrantDao entrantDao;

    @InjectComponent
    private Grid grid;

    @InjectComponent
    private Zone personsZone;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    void onValueChangedFromNumberOfEntrant(int numberOfEntrant){
        this.numberOfEntrant = numberOfEntrant;
        if (request.isXHR()) {
            ajaxResponseRenderer.addRender(personsZone);
        }
    }

    void onSuccessFromFilterCriteria() {
        if (request.isXHR()) {
            ajaxResponseRenderer.addRender(personsZone);
        }
    }

    public GridDataSource getListRows() {
        return new PersonFilteredDataSource(entrantDao, filterLastName, filterFirstName, filterMiddleName, filterCaseNumber);
    }

    public class PersonFilteredDataSource implements GridDataSource {
        private EntrantDao entrantDao;
        private String filterLastName;
        private String filterFirstName;
        private String filterMiddleName;
        private String filterCaseNumber;

        private int startIndex;
        private java.util.List<EntrantFacade> preparedResults;

        public PersonFilteredDataSource(EntrantDao entrantDao, String filterLastName,
                                        String filterFistName, String filterMiddleName, String filterCaseNumber) {
            this.entrantDao = entrantDao;
            this.filterLastName = filterLastName;
            this.filterFirstName = filterFistName;
            this.filterMiddleName = filterMiddleName;
            this.filterCaseNumber = filterCaseNumber;
        }

        @Override
        public int getAvailableRows() {
            return (int) entrantDao.countEntrants(filterLastName, filterFirstName, filterMiddleName, filterCaseNumber);
        }

        @Override
        public void prepare(final int startIndex, final int endIndex, final java.util.List<SortConstraint> sortConstraints) {

            java.util.List<Entrant> list = entrantDao.findByFilter(filterLastName, filterFirstName, filterMiddleName, filterCaseNumber, startIndex, endIndex, sortConstraints);
            preparedResults = new ArrayList<>();

            for(Entrant entrant: list){
                EntrantFacade entrantFacade = new EntrantFacade(entrant);
                preparedResults.add(entrantFacade);
            }

            this.startIndex = startIndex;
        }

        @Override
        public Object getRowValue(final int index) {
            return preparedResults.get(index - startIndex);
        }

        @Override
        public Class<EntrantFacade> getRowType() {
            return EntrantFacade.class;
        }
    }
}
