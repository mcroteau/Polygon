package shape.support;

import qio.annotate.Config;
import qio.annotate.Dependency;
import qio.annotate.Property;
import qio.jdbc.BasicDataSource;

import javax.sql.DataSource;

@Config
public class DbConfig {

    @Property("db.url")
    String dbUrl;

    @Property("db.user")
    String dbUser;

    @Property("db.pass")
    String dbPass;

    @Property("db.driver")
    String dbDriver;

    @Dependency
    public DataSource dataSource(){
//        return new Papi.New()
//                .connections(23)
//                .url(dbUrl)
//                .driver(dbDriver)
//                .user(dbUser)
//                .password(dbPass)
//                .make();
        return new BasicDataSource.Builder()
                .url(dbUrl)
                .driver(dbDriver)
                .username(dbUser)
                .password(dbPass)
                .build();
    }

}
