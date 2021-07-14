package core;

import java.sql.Connection;

/**
 * @author afmika
 */
public interface afQueryable {
    // main overridable requests
    public int save (Connection connection) throws Exception;
    public int remove (Connection connection) throws Exception;
}
