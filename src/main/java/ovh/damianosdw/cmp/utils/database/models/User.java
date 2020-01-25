/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "users")
public class User
{
    @DatabaseField(columnName = "user_id", unique = true, generatedId = true, canBeNull = false)
    private long userId;
    @DatabaseField(columnName = "employee_id", foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, canBeNull = false)
    private Employee employee;
    @DatabaseField(columnName = "login", unique = true, canBeNull = false)
    private String login;
    @DatabaseField(columnName = "password", canBeNull = false)
    private String password;
    @DatabaseField(columnName = "group_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private UserGroup userGroup;
    @DatabaseField(columnName = "active", canBeNull = false)
    private boolean active;
}
