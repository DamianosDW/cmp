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
@DatabaseTable(tableName = "user_groups")
public class UserGroup
{
    @DatabaseField(columnName = "group_id", unique = true, generatedId = true, canBeNull = false)
    private long groupId;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;
}
