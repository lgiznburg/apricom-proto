package ru.apricom.testapp.entities.catalogs;

/**
 * @author leonid.
 */
public enum EducationLevelType {
    BASE_COMMON("среднее общее", "СОО",1 ),
    BASE_PROFESSIONAL("среднее профессиональное", "СПО", 1 ),
    HIGH_BACHELOR("Высшее - бакалавриат","бакалавриат", 2 ),
    HIGH_MASTER("Высшее - магистратура", "магистратура", 3 ),
    HIGH_SPECIALITY("Высшее - специалитет", "специалитет", 3 ),
    RESIDENCY("Постдипломное - ординатура", "ординатура", 4 ),
    POSTGRADUATE("Постдипломное - аспирантура", "аспирантура", 4 );

    private String title;
    private String title_short;
    private int comparisonLevel;

    EducationLevelType( String title, String title_short, int comparisonLevel ) {
        this.title = title;
        this.title_short = title_short;
        this.comparisonLevel = comparisonLevel;
    }

    public String getTitle() {
        return title;
    }

    public String getShortTitle() { return title_short; }

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
