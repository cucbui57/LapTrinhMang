package Control.ServerController;

import java.sql.Connection;

public abstract class Handle {
    Connection connection;
    public abstract void execute();
}
