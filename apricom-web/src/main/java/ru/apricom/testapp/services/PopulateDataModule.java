package ru.apricom.testapp.services;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import ru.apricom.seedentity.SeedEntityIdentifier;
import ru.apricom.seedentity.SeedEntityUpdater;
import ru.apricom.seedentity.hibernate.SeedEntity;
import ru.apricom.testapp.entities.auth.GrantedPermission;
import ru.apricom.testapp.entities.auth.RolesNames;
import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.auth.UserRole;
import ru.apricom.testapp.entities.base.Competition;
import ru.apricom.testapp.entities.base.EducationalProgram;
import ru.apricom.testapp.entities.base.ProgramRequirement;
import ru.apricom.testapp.entities.base.Speciality;
import ru.apricom.testapp.entities.catalogs.*;
import ru.apricom.testapp.entities.catalogs.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * @author leonid.
 */
public class PopulateDataModule {

    //populate with initial values
    @Contribute(SeedEntity.class)
    public static void addSeedEntities( OrderedConfiguration<Object> configuration) {
        populateUsers( configuration );
        populateBaseObjects( configuration );
        populateCountries( configuration );
    }

    private static void populateUsers( OrderedConfiguration<Object> configuration ) {

        GrantedPermission createEntrant = new GrantedPermission("entrant:create");
        GrantedPermission acceptEntrant = new GrantedPermission("entrant:accept");
        GrantedPermission editEntrant = new GrantedPermission("entrant:edit");
        configuration.add( editEntrant.getPermission(), editEntrant );
        configuration.add( acceptEntrant.getPermission(), acceptEntrant );
        configuration.add( createEntrant.getPermission(), createEntrant );

        //Roles:
        UserRole adminRole = null;
        UserRole headMaster = null;
        for ( RolesNames roleName : RolesNames.values() ) {
            UserRole role = new UserRole();
            role.setAuthority( roleName.name() );
            role.setPermissions( new HashSet<>() );
            configuration.add( roleName.name(), role );

            if ( roleName == RolesNames.SYSTEM_ADMIN ) {
                adminRole = role;
                role.getPermissions().add( editEntrant );
            }
            if ( roleName == RolesNames.HEADMASTER ) {
                headMaster = role;
                role.getPermissions().add( acceptEntrant );
            }
            if ( roleName == RolesNames.ENTRANT ) {
                role.getPermissions().add( createEntrant );
            }
            if ( roleName == RolesNames.CHIEF_HEADMASTER ) {
                role.getPermissions().add( editEntrant );
                role.getPermissions().add( acceptEntrant );
            }
            configuration.add( roleName.name() + "_update", new SeedEntityUpdater( role, role, true ) );
        }

        User admin = new User();
        admin.setUsername( "prk_admin@rsmu.ru" );
        admin.setFirstName( "Эдельвейс" );
        admin.setMiddleName( "Захарович" );
        admin.setLastName( "Машкин" );
        admin.setPassword( "96e79218965eb72c92a549dd5a330112" );
        admin.setRoles( new LinkedList<>() );
        admin.getRoles().add( adminRole );
        configuration.add( "admin_super", admin );

        User me = new User();
        me.setUsername( "ginzburg_ld@rsmu.ru" );
        me.setFirstName( "Леонид" );
        me.setMiddleName( "Давидович" );
        me.setLastName( "Гинзбург" );
        me.setPassword( "33A1628D8BF774777141A6AF3B283DC4" );
        me.setRoles( new LinkedList<>() );
        me.getRoles().add( adminRole );
        configuration.add( "me", me );

        User pps = new User();
        pps.setUsername( "polyakov_ps@rsmu.ru" );
        pps.setFirstName( "Павел" );
        pps.setMiddleName( "Сергеевич" );
        pps.setLastName( "Поляков" );
        pps.setPassword( "25D3B13C5501D00F0E63E77CD8F6B4BB" );
        pps.setRoles( new LinkedList<>() );
        pps.getRoles().add( adminRole );
        configuration.add( "pps", pps );

        User headMaser = new User();
        headMaser.setUsername( "headmaster@rsmu.ru" );
        headMaser.setFirstName( "Студент" );
        headMaser.setMiddleName( "Ординаторович" );
        headMaser.setLastName( "Столоначальников" );
        headMaser.setPassword( "96e79218965eb72c92a549dd5a330112" );
        headMaser.setRoles( new LinkedList<>() );
        headMaser.getRoles().add( headMaster );
        configuration.add( "headMasterUser", headMaser );
    }

    private static void populateBaseObjects( OrderedConfiguration<Object> configuration ) {
        configuration.add( "admissionType", new SeedEntityIdentifier( AdmissionType.class, "title" ) );
        AdmissionType baseAdmission = new AdmissionType( 1, "общий конкурс", "общий");
        configuration.add("BASE_ADMISSION", baseAdmission );
        AdmissionType targetAdmission = new AdmissionType(2, "целевой прием", "целевой");
        configuration.add("TARGET_ADMISSION", targetAdmission );
        AdmissionType noExamsAdmission = new AdmissionType(3, "без вступительных испытаний", "без ВИ");
        configuration.add("NO_EXAMS", noExamsAdmission );
        AdmissionType specialQuoteAdmission = new AdmissionType(4, "особая квота", "по квоте");
        configuration.add("SPECIAL_QUOTE", specialQuoteAdmission );
        AdmissionType contractAdmission = new AdmissionType(5, "по договору", "по договору");
        configuration.add( "CONTRACT", contractAdmission );

        configuration.add( "documentType", new SeedEntityIdentifier( DocumentType.class, "title" ) );
        configuration.add( "PERSON_ID", new DocumentType( 1, "Удостоверение личности", "УЛ" ) );
        configuration.add( "EDU_DOCUMENT", new DocumentType( 2, "Документ об образовании", "ДОО" ) );
        configuration.add( "SNILS", new DocumentType( 3, "СНИЛС", "СНИЛС" ) );
        configuration.add( "PHOTO", new DocumentType( 4, "Фото", "фото" ) );
        configuration.add( "OTHER", new DocumentType( 5, "Другой документ", "док." ) );

        configuration.add( "EducationalSubject", new SeedEntityIdentifier( EducationalSubject.class, "title" ) );
        EducationalSubject chemistry = new EducationalSubject( 1, "химия", "хим.");
        configuration.add( "CHEMISTRY", chemistry );
        EducationalSubject biology = new EducationalSubject( 2, "биология", "био.");
        configuration.add( "BIOLOGY", biology );
        EducationalSubject russian = new EducationalSubject( 3, "русский язык", "рус.яз.");
        configuration.add( "RUSSIAN", russian );
        EducationalSubject math = new EducationalSubject( 4, "математика", "мат.");
        configuration.add( "MATH", math );
        EducationalSubject physics = new EducationalSubject( 5, "физика", "физ.");
        configuration.add( "PHYSICS", physics );
        EducationalSubject history = new EducationalSubject( 6, "история", "ист.");
        configuration.add( "HISTORY", history );
        EducationalSubject social = new EducationalSubject( 7, "обществознание", "общ.");
        configuration.add( "SOCIAL", social );
        EducationalSubject info = new EducationalSubject( 8, "информатика", "инф.");
        configuration.add( "INFO", info );
        configuration.add( "ENGLISH", new EducationalSubject( 9, "английский язык", "англ.яз.") );
        configuration.add( "FRENCH", new EducationalSubject( 10, "французский язык", "фр.яз.") );
        configuration.add( "GERMAN", new EducationalSubject( 11, "немецкий язык", "нем.яз.") );
        //configuration.add( "", new EducationalSubject( 1, "", "") );
        //configuration.add( "", new EducationalSubject( 1, "", "") );

        configuration.add( "EducationBase", new SeedEntityIdentifier( EducationBase.class, "title" ) );
        configuration.add( "BUDGET", new EducationBase( FinancingType.BUDGET.ordinal()+1, "бюджет", "бюджет") );
        configuration.add( "CONTRACT", new EducationBase( FinancingType.CONTRACT.ordinal()+1, "договор", "договор") );

        configuration.add( "EducationDocumentType", new SeedEntityIdentifier( EducationDocumentType.class, "title" ) );
        configuration.add( "DIPLOMA", new EducationDocumentType( 1, "Аттестат о среднем общем образовании", "АОО") );
        configuration.add( "PROF_DIPLOMA", new EducationDocumentType( 2, "Диплом о среднем специальном образовании", "ДСПО") );
        configuration.add( "HIGH_SCHOOL_DIPLOMA", new EducationDocumentType( 3, "Диплом о высшем образовании", "ДВПО") );

        configuration.add( "EducationForm", new SeedEntityIdentifier( EducationForm.class, "title" ) );
        EducationForm fullTimeForm = new EducationForm( 1, "очная форма обучения", "очное");
        configuration.add( "FULL_TIME", fullTimeForm );
        configuration.add( "DISTANCE", new EducationForm( 2, "заочная форма обучения", "заочное") );
        configuration.add( "EVENING", new EducationForm( 3, "очно-заочная форма обучения", "вечернее") );

        configuration.add( "EducationLevel", new SeedEntityIdentifier( EducationLevel.class, "title" ) );
        configuration.add( "BASE_EDU", new EducationLevel( 1, "среднее", "среднее") );
        configuration.add( "SPECIAL_EDU", new EducationLevel( 2, "среднее специальное", "специальное") );
        configuration.add( "HIGH_EDU", new EducationLevel( 3, "высшее", "высшее") );

        configuration.add( "IdDocumentType", new SeedEntityIdentifier( IdDocumentType.class, "title" ) );
        configuration.add( "PASSPORT", new IdDocumentType( 1, "Паспорт", "паспорт") );
        configuration.add( "FOREIGN_PASSPORT", new IdDocumentType( 2, "Паспорт иностранного гражданина", "паспорт ино") );
        configuration.add( "MILITARY", new IdDocumentType( 3, "Военный билет", "военный") );


        Speciality therapy = new Speciality();
        therapy.setCode( "31.05.01" );
        therapy.setTitle( "Лечебное дело" );
        therapy.setShortTitle( "леч.дело" );
        therapy.setLevel( EducationLevelType.HIGH_SPECIALITY );
        configuration.add( "THERAPY", therapy );

        Speciality pediatric = new Speciality();
        pediatric.setCode( "31.05.02" );
        pediatric.setTitle( "Педиатрия" );
        pediatric.setShortTitle( "пед." );
        pediatric.setLevel( EducationLevelType.HIGH_SPECIALITY );
        configuration.add( "PEDIATRIC", pediatric );

        Speciality dentistry = new Speciality();
        dentistry.setCode( "31.05.03" );
        dentistry.setTitle( "Стоматология" );
        dentistry.setShortTitle( "стомат." );
        dentistry.setLevel( EducationLevelType.HIGH_SPECIALITY );
        configuration.add( "DENTISTRY", dentistry );

        Speciality bioChemistry = new Speciality();
        bioChemistry.setCode( "30.05.01" );
        bioChemistry.setTitle( "Медицинская биохимия" );
        bioChemistry.setShortTitle( "биохимия" );
        bioChemistry.setLevel( EducationLevelType.HIGH_SPECIALITY );
        configuration.add( "BIO_CHEMISTRY", bioChemistry );

        Speciality bioPhysics = new Speciality();
        bioPhysics.setCode( "30.05.02" );
        bioPhysics.setTitle( "Медицинская биофизика" );
        bioPhysics.setShortTitle( "биофизика" );
        bioPhysics.setLevel( EducationLevelType.HIGH_SPECIALITY );
        configuration.add( "BIO_PHYSICS", bioPhysics );

        Speciality cybernetics = new Speciality();
        cybernetics.setCode( "30.05.03" );
        cybernetics.setTitle( "Медицинская кибернетика" );
        cybernetics.setShortTitle( "мед.кибернетика" );
        cybernetics.setLevel( EducationLevelType.HIGH_SPECIALITY );
        configuration.add( "CYBERNETICS", cybernetics );

        Speciality pharmacy = new Speciality();
        pharmacy.setCode( "33.05.01" );
        pharmacy.setTitle( "Фармация" );
        pharmacy.setShortTitle( "фарм." );
        pharmacy.setLevel( EducationLevelType.HIGH_SPECIALITY );
        configuration.add( "PHARMACY", pharmacy );

        Speciality psychology = new Speciality();
        psychology.setCode( "37.05.01" );
        psychology.setTitle( "Клиническая  психология" );
        psychology.setShortTitle( "клин.псих." );
        psychology.setLevel( EducationLevelType.HIGH_SPECIALITY );
        configuration.add( "PSYCHOLOGY", psychology );

        Speciality socialWork = new Speciality();
        socialWork.setCode( "39.03.02" );
        socialWork.setTitle( "Социальная работа" );
        socialWork.setShortTitle( "соц.работа" );
        socialWork.setLevel( EducationLevelType.HIGH_BACHELOR );
        configuration.add( "SOCIAL_WORK", socialWork );

        Speciality medBiology = new Speciality();
        medBiology.setCode( "06.03.01" );
        medBiology.setTitle( "Биология" );
        medBiology.setShortTitle( "биология" );
        medBiology.setLevel( EducationLevelType.HIGH_BACHELOR );
        configuration.add( "MED_BIOLOGY", medBiology );

        EducationalProgram therapyProgram = new EducationalProgram( therapy, fullTimeForm, 12, EducationLevelType.BASE_COMMON );
        configuration.add( "THERAPY_PROGRAM", therapyProgram );
        EducationalProgram pediatricProgram = new EducationalProgram( pediatric, fullTimeForm, 12, EducationLevelType.BASE_COMMON );
        configuration.add( "pediatric_PROGRAM", pediatricProgram );
        EducationalProgram pharmacyProgram = new EducationalProgram( pharmacy, fullTimeForm, 12, EducationLevelType.BASE_COMMON );
        configuration.add( "pharmacy_PROGRAM", pharmacyProgram );
        EducationalProgram dentistryProgram = new EducationalProgram( dentistry, fullTimeForm, 12, EducationLevelType.BASE_COMMON );
        configuration.add( "dentistry_PROGRAM", dentistryProgram );
        EducationalProgram bioChemistryProgram = new EducationalProgram( bioChemistry, fullTimeForm, 12, EducationLevelType.BASE_COMMON );
        configuration.add( "bioChemistry_PROGRAM", bioChemistryProgram );
        EducationalProgram bioPhysicsProgram = new EducationalProgram( bioPhysics, fullTimeForm, 12, EducationLevelType.BASE_COMMON );
        configuration.add( "bioPhysics_PROGRAM", bioPhysicsProgram );
        EducationalProgram cyberneticsProgram = new EducationalProgram( cybernetics, fullTimeForm, 12, EducationLevelType.BASE_COMMON );
        configuration.add( "cybernetics_PROGRAM", cyberneticsProgram );
        EducationalProgram psychologyProgram = new EducationalProgram( psychology, fullTimeForm, 12, EducationLevelType.BASE_COMMON );
        configuration.add( "psychology_PROGRAM", psychologyProgram );
        EducationalProgram socialWorkProgram = new EducationalProgram( socialWork, fullTimeForm, 8, EducationLevelType.BASE_COMMON );
        configuration.add( "socialWork_PROGRAM", socialWorkProgram );
        EducationalProgram medBiologyProgram = new EducationalProgram( medBiology, fullTimeForm, 8, EducationLevelType.BASE_COMMON );
        configuration.add( "medBiology_PROGRAM", medBiologyProgram );

        configuration.add( "therapyProgram_1", new ProgramRequirement( therapyProgram, chemistry, 50, 1 ) );
        configuration.add( "therapyProgram_2", new ProgramRequirement( therapyProgram, biology, 50, 2 ) );
        configuration.add( "therapyProgram_3", new ProgramRequirement( therapyProgram, russian, 50, 3 ) );

        configuration.add( "pediatricProgram_1", new ProgramRequirement( pediatricProgram, chemistry, 50, 1 ) );
        configuration.add( "pediatricProgram_2", new ProgramRequirement( pediatricProgram, biology, 50, 2 ) );
        configuration.add( "pediatricProgram_3", new ProgramRequirement( pediatricProgram, russian, 50, 3 ) );

        configuration.add( "pharmacyProgram_1", new ProgramRequirement( pharmacyProgram, chemistry, 50, 1 ) );
        configuration.add( "pharmacyProgram_2", new ProgramRequirement( pharmacyProgram, biology, 50, 2 ) );
        configuration.add( "pharmacyProgram_3", new ProgramRequirement( pharmacyProgram, russian, 50, 3 ) );

        configuration.add( "dentistryProgram_1", new ProgramRequirement( dentistryProgram, chemistry, 50, 1 ) );
        configuration.add( "dentistryProgram_2", new ProgramRequirement( dentistryProgram, biology, 50, 2 ) );
        configuration.add( "dentistryProgram_3", new ProgramRequirement( dentistryProgram, russian, 50, 3 ) );

        configuration.add( "bioChemistryProgram_1", new ProgramRequirement( bioChemistryProgram, chemistry, 50, 1 ) );
        configuration.add( "bioChemistryProgram_2", new ProgramRequirement( bioChemistryProgram, biology, 50, 2 ) );
        configuration.add( "bioChemistryProgram_3", new ProgramRequirement( bioChemistryProgram, russian, 50, 3 ) );

        configuration.add( "bioPhysicsProgram_1", new ProgramRequirement( bioPhysicsProgram, physics, 50, 1 ) );
        configuration.add( "bioPhysicsProgram_2", new ProgramRequirement( bioPhysicsProgram, math, 50, 2 ) );
        configuration.add( "bioPhysicsProgram_3", new ProgramRequirement( bioPhysicsProgram, russian, 50, 3 ) );

        configuration.add( "cyberneticsProgram_1", new ProgramRequirement( cyberneticsProgram, math, 50, 1 ) );
        configuration.add( "cyberneticsProgram_2", new ProgramRequirement( cyberneticsProgram, biology, 50, 2 ) );
        configuration.add( "cyberneticsProgram_3", new ProgramRequirement( cyberneticsProgram, russian, 50, 3 ) );

        configuration.add( "psychologyProgram_1", new ProgramRequirement( psychologyProgram, biology, 45, 1 ) );
        configuration.add( "psychologyProgram_2", new ProgramRequirement( psychologyProgram, math, 45, 2 ) );
        configuration.add( "psychologyProgram_3", new ProgramRequirement( psychologyProgram, russian, 45, 3 ) );

        configuration.add( "socialWorkProgram_1", new ProgramRequirement( socialWorkProgram, chemistry, 45, 1 ) );
        configuration.add( "socialWorkProgram_2", new ProgramRequirement( socialWorkProgram, biology, 45, 2 ) );
        configuration.add( "socialWorkProgram_3", new ProgramRequirement( socialWorkProgram, russian, 45, 3 ) );

        configuration.add( "medBiologyProgram_1", new ProgramRequirement( medBiologyProgram, biology, 50, 1 ) );
        configuration.add( "medBiologyProgram_2", new ProgramRequirement( medBiologyProgram, math, 50, 2 ) );
        configuration.add( "medBiologyProgram_3", new ProgramRequirement( medBiologyProgram, russian, 50, 3 ) );

        AdmissionType[] admissionTypes = new AdmissionType[] {noExamsAdmission, specialQuoteAdmission, targetAdmission, baseAdmission };
        EducationalProgram[] educationalPrograms = new EducationalProgram[] {
                therapyProgram, pediatricProgram, pharmacyProgram, dentistryProgram,
                bioChemistryProgram, bioPhysicsProgram, cyberneticsProgram,
                psychologyProgram, socialWorkProgram, medBiologyProgram
        };

        for ( EducationalProgram program : educationalPrograms ) {
            int sequenceNumber = 1;
            for ( AdmissionType admissionType : admissionTypes ) {
                configuration.add( program.getSpeciality().getTitle()+admissionType.getShortTitle(),
                        new Competition( admissionType, program, FinancingType.BUDGET, sequenceNumber++ ) );
            }
            configuration.add( program.getSpeciality().getTitle()+contractAdmission.getShortTitle(),
                    new Competition( contractAdmission, program, FinancingType.CONTRACT, sequenceNumber ) );
        }
    }

    private static void populateCountries( OrderedConfiguration<Object> configuration ) {
        InputStream is = PopulateDataModule.class
                .getClassLoader().getResourceAsStream( "catalogs/countries.csv" );
        if ( is != null ) {
            BufferedReader br = new BufferedReader(new InputStreamReader( is, StandardCharsets.UTF_8));
            boolean skipFirstLine = true;
            try {
                for ( String strLine; (strLine = br.readLine()) != null; ) {
                    if ( skipFirstLine ) {
                        skipFirstLine = false;
                        continue;
                    }
                    String[] parts = strLine.split( ";" );
                    if ( parts.length > 5 ) {
                        Country country = new Country( parts[0], parts[1], parts[2], parts[3], parts[4] );
                        configuration.add( parts[3], country );
                    }
                }
            } catch (IOException e) {
                // Unable to read countries
            }
        }
    }
}
