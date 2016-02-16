package flyway.elf;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.db.ViewHelper;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;

/**
 * Created by PHELESUO on 15.2.2016.
 */
public class V1_11__add_ol3_publisher_template implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        ViewHelper.insertView(connection, "elf-publisher-template-view-ol3.json");
    }
}
