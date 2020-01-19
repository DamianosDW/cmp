/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils.database;

public class DatabaseColumnBuilder
{
    private String name;
    private String type;
    private boolean primaryKey = false;
    private boolean notNull;

    public DatabaseColumnBuilder(String name, String type)
    {
        this.name = name;
        this.type = type;
    }

    public static DatabaseColumnBuilder builder(String name, String type)
    {
        return new DatabaseColumnBuilder(name, type);
    }

    public DatabaseColumnBuilder primaryKey(boolean primaryKey)
    {
        this.primaryKey = primaryKey;
        this.notNull = true;
        return this;
    }

    public DatabaseColumnBuilder notNull(boolean notNull)
    {
        this.notNull = notNull;
        return this;
    }

    public DatabaseColumn build()
    {
        return new DatabaseColumn(name, type, primaryKey, notNull);
    }
}
