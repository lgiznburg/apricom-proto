package ru.apricom.testapp.entities.catalogs;

/**
 * @author leonid.
 */
public enum EducationLevelType {
    BASE_COMMON("среднее общее", 1 ),
    BASE_PROFESSIONAL("среднее специальное", 1 ),
    HIGH_BACHELOR("бакалавриат", 2 ),
    HIGH_MASTER("магистратура", 3 ),
    HIGH_SPECIALITY("специалитет", 3 ),
    RESIDENCY("ординатура", 4 ),
    POSTGRADUATE("аспирантура", 4 );

    private String title;
    
    private int comparisonLevel;

    EducationLevelType( String title, int comparisonLevel ) {
        this.title = title;
        this.comparisonLevel = comparisonLevel;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Check if given level of education meets requirement of this level.
     * This comparison it not transitive.
     * @param compareTo Education level to check requirement
     * @return true if compareTo level if enough to start education on this level.
     */
    public boolean isMeetRequirement( EducationLevelType compareTo ) {
        if (compareTo == null ) {
            return false;
        }
        if ( this.equals( compareTo ) ) {
            return true;
        }

        return this.comparisonLevel <= compareTo.comparisonLevel;
    }
}
