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
import ru.apricom.testapp.dao.RequestDao;
import ru.apricom.testapp.entities.catalogs.AdmissionType;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * @author leonid.
 */
@RequiresUser
@Import (stylesheet = "context:static/css/requestList.css")
public class List {
    static private final int MAX_RESULTS = 30;

    // Screen fields

    @Property
    private java.util.List<String> titles;

    @Property
    @ActivationRequestParameter(value = "title", required = false)
    private String title;

    //@Property
    private java.util.List<AdmissionType> listRows;

    // Generally useful bits and pieces

    @Inject
    private RequestDao requestDao;

    @InjectComponent
    private Grid grid;

    @InjectComponent
    private Zone personsZone;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    // The code

    void setupRender() {
        // Get all persons - ask business service to find them (from the database)
        listRows = requestDao.findAll(AdmissionType.class);

        if (grid.getSortModel().getSortConstraints().isEmpty()) {
            grid.getSortModel().updateSort("title");
        }
        titles = new ArrayList<String>();
        for ( AdmissionType type : listRows ) {
            titles.add( type.getTitle() );
        }
        if ( title == null ) {
            title = "";
        }
    }

    void onValidateFromFilterCriteria() {
        if (request.isXHR()) {
            ajaxResponseRenderer.addRender(personsZone);
        }
    }

    public GridDataSource getListRows() {
        return new PersonFilteredDataSource(requestDao, title);
    }

    public class PersonFilteredDataSource implements GridDataSource {
        private RequestDao requestDao;
        private String title;

        private int startIndex;
        private java.util.List<AdmissionType> preparedResults;

        public PersonFilteredDataSource(RequestDao requestDao, String title) {
            this.requestDao = requestDao;
            this.title = title;
        }

        @Override
        public int getAvailableRows() {
            return (int) requestDao.countAdmissionType(title);
        }

        @Override
        public void prepare(final int startIndex, final int endIndex, final java.util.List<SortConstraint> sortConstraints) {

            // Get a filtered page of persons - ask business service to find them (from the database)

            /*java.util.List<SortCriterion> sortCriteria = toSortCriteria(sortConstraints);
            preparedResults = personFinderService.findPersons(firstInitial, lastInitial, region, startIndex, endIndex
                    - startIndex + 1, sortCriteria);*/
            //preparedResults = requestDao.findAll(AdmissionType.class);
            preparedResults = requestDao.findByTitle(title, startIndex, endIndex, sortConstraints);

            this.startIndex = startIndex;
        }

        @Override
        public Object getRowValue(final int index) {
            return preparedResults.get(index - startIndex);
        }

        @Override
        public Class<AdmissionType> getRowType() {
            return AdmissionType.class;
        }

        /**
         * Converts a list of Tapestry's SortConstraint to a list of our business tier's SortCriterion. The business tier
         * does not use SortConstraint because that would create a dependency on Tapestry.
         */
 /*       private List<SortCriterion> toSortCriteria(List<SortConstraint> sortConstraints) {
            List<SortCriterion> sortCriteria = new ArrayList<SortCriterion>();

            for (SortConstraint sortConstraint : sortConstraints) {

                String propertyName = sortConstraint.getPropertyModel().getPropertyName();
                SortDirection sortDirection = SortDirection.UNSORTED;

                switch (sortConstraint.getColumnSort()) {
                    case ASCENDING:
                        sortDirection = SortDirection.ASCENDING;
                        break;
                    case DESCENDING:
                        sortDirection = SortDirection.DESCENDING;
                        break;
                    default:
                }

                SortCriterion sortCriterion = new SortCriterion(propertyName, sortDirection);
                sortCriteria.add(sortCriterion);
            }

            return sortCriteria;
        }
*/
    }
}
