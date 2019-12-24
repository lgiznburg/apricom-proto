package ru.apricom.testapp.services;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import ru.apricom.seedentity.hibernate.SeedEntity;
import ru.apricom.testapp.entities.auth.RolesNames;
import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.auth.UserRole;
import ru.apricom.testapp.entities.catalogs.AdmissionType;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * @author leonid.
 */
public class PopulateDataModule {

    //populate with initial values
    @Contribute(SeedEntity.class)
    public static void addSeedEntities( OrderedConfiguration<Object> configuration) {
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
            }
            if ( roleName == RolesNames.HEADMASTER ) {
                headMaster = role;
            }
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

/*
        configuration.add("BASE_ADMISSION", new AdmissionType( 1, "общий конкурс", "общий"));
        configuration.add("TARGET_ADMISSION", new AdmissionType(2, "целевой прием", "целевой"));
        configuration.add("NO_EXAMS", new AdmissionType(3, "без вступительных испытаний", "без ВИ"));
        configuration.add("SPECIAL_QUOTE", new AdmissionType(4, "особая квота", "по квоте"));
        configuration.add( "CONTRACT", new AdmissionType(5, "по договору", "по договору"));
*/
    }
}
