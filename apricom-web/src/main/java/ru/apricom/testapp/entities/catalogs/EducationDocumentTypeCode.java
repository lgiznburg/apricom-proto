package ru.apricom.testapp.entities.catalogs;

public enum EducationDocumentTypeCode {
    BASIC_CERTIFICATE( "Аттестат о среднем общем образовании", "АСОО" ),
    BASIC_PROFESSIONAL_DIPLOMA( "Диплом о среднем профессиональном образовании", "ДСПО" ),
    BACHELOR_DIPLOMA( "Диплом бакалавра", "ДБ" ),
    MASTER_DIPLOMA ( "Диплом магистра", "ДМ" ),
    SPECIALIST_DIPLOMA( "Диплом специалиста", "ДС" ),
    RESIDENT_CERTIFICATE( "Сертификат специалиста", "СО" ),
    POSTGRADUATE_CERTIFICATE( "Сертификат о прохождении постдипломной подготовки", "СА" );

    private String name;
    private String name_short;

    EducationDocumentTypeCode( String name, String name_short ) {
        this.name = name;
        this.name_short = name_short;
    }

    public String getName() {
        return name;
    }

    public String getName_short() {
        return name_short;
    }
}
