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
@DatabaseTable(tableName = "work_holidays")
public class WorkHoliday
{
    @DatabaseField(columnName = "holiday_id", unique = true, generatedId = true, canBeNull = false)
    private long holidayId;
    @DatabaseField(columnName = "employee_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Employee employee;
    @DatabaseField(columnName = "start_date", canBeNull = false, format = "dd-MM-yyyy", persisterClass = CustomDatePersister.class)
    private CustomDate startDate;
    @DatabaseField(columnName = "end_date", canBeNull = false, format = "dd-MM-yyyy", persisterClass = CustomDatePersister.class)
    private CustomDate endDate;
    @DatabaseField(columnName = "type", canBeNull = false)
    private String type;
    @DatabaseField(columnName = "reason", dataType = DataType.LONG_STRING)
    private String reason;
    @DatabaseField(columnName = "status", canBeNull = false, defaultValue = "OCZEKIWANIE")
    private String status;
}
