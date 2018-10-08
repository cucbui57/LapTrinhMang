package Control.ServerController;

import java.sql.Connection;

public abstract class Handle<T> {
    Connection connection; 
    abstract T execute();
}
