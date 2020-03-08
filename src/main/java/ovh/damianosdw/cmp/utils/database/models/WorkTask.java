/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils.database.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ovh.damianosdw.cmp.misc.CustomDate;
import ovh.damianosdw.cmp.utils.database.persisters.CustomDatePersister;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "work_tasks")
public class WorkTask
{
    @DatabaseField(columnName = "task_id", generatedId = true, unique = true, canBeNull = false)
    private long taskId;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;
    @DatabaseField(columnName = "status", canBeNull = false)
    private String status;
    @DatabaseField(columnName = "creator", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Employee creator;
    @DatabaseField(columnName = "assigned_to", foreign = true, foreignAutoRefresh = true)
    private Employee assignedTo;
    @DatabaseField(columnName = "creation_date", canBeNull = false, persisterClass = CustomDatePersister.class)
    private CustomDate creationDate;
    @DatabaseField(columnName = "update_date", persisterClass = CustomDatePersister.class)
    private CustomDate updateDate;
    @DatabaseField(columnName = "additional_info", dataType = DataType.LONG_STRING)
    private String additionalInfo;
}
