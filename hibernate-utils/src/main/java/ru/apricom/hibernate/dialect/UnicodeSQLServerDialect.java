package ru.apricom.hibernate.dialect;

import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 * @author leonid.
 *
 * Dialect for MS SQL server to support i18n 
 * Copied from internet. Use nvarchar for all character and string types
 */
public class UnicodeSQLServerDialect extends SQLServerDialect {
    public UnicodeSQLServerDialect() {
        super();
        registerColumnType(Types.CHAR, "nchar(1)");
        registerColumnType(Types.LONGVARCHAR, "nvarchar(max)" );
        registerColumnType(Types.VARCHAR, 4000, "nvarchar($l)");
        registerColumnType(Types.VARCHAR, "nvarchar(max)");
        registerColumnType(Types.CLOB, "nvarchar(max)" );

        registerColumnType(Types.NCHAR, "nchar(1)");
        registerColumnType(Types.LONGNVARCHAR, "nvarchar(max)");
        registerColumnType(Types.NVARCHAR, 4000, "nvarchar($l)");
        registerColumnType(Types.NVARCHAR, "nvarchar(max)");
        registerColumnType(Types.NCLOB, "nvarchar(max)");

        registerHibernateType(Types.NCHAR, StandardBasicTypes.CHARACTER.getName());
        registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.TEXT.getName());
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType( Types.NCLOB, StandardBasicTypes.CLOB.getName() );

    }
}
