package ru.apricom.testapp.services;

import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Match;
import ru.apricom.seedentity.hibernate.SeedEntity;
import ru.apricom.seedentity.hibernate.SeedEntityImpl;
import ru.apricom.testapp.dao.CatalogDao;
import ru.apricom.testapp.dao.CountryDao;
import ru.apricom.testapp.dao.EntrantDao;
import ru.apricom.testapp.dao.UserDao;
import ru.apricom.testapp.dao.implementaion.CatalogDaoImpl;
import ru.apricom.testapp.dao.implementaion.CountryDaoImpl;
import ru.apricom.testapp.dao.implementaion.EntrantDaoImpl;
import ru.apricom.testapp.dao.implementaion.UserDaoImpl;

/**
 * @author leonid.
 *
 */
@ImportModule( PopulateDataModule.class )
public class HibernateModule {

    public static void bind( ServiceBinder binder)
    {
        binder.bind( UserDao.class, UserDaoImpl.class );
        binder.bind( CountryDao.class, CountryDaoImpl.class );
        binder.bind( EntrantDao.class, EntrantDaoImpl.class );
        binder.bind( CatalogDao.class, CatalogDaoImpl.class );

        //seed entity - initial DB population
        binder.bind( SeedEntity.class, SeedEntityImpl.class);
    }

    @Match("*Dao")
    public static void adviseTransactions( HibernateTransactionAdvisor advisor,
                                           MethodAdviceReceiver receiver)
    {
        // any method in classes "*Dao" annotated with @CommitAfter will be encapsulated into transaction
        advisor.addTransactionCommitAdvice(receiver);
    }
}
