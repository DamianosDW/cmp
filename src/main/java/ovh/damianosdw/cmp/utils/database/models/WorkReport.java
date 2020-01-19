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
@DatabaseTable(tableName = "work_reports")
public class WorkReport
{
    @DatabaseField(columnName = "report_id", unique = true, generatedId = true, canBeNull = false)
    private long reportId;
    @DatabaseField(columnName = "sent_date", canBeNull = false, format = "dd-MM-yyyy HH:mm:ss", persisterClass = CustomDatePersister.class)
    private CustomDate sentDate;
    @DatabaseField(columnName = "employee_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Employee employee;
    @DatabaseField(columnName = "work_report", canBeNull = false, dataType = DataType.LONG_STRING)
    private String workReport;
}
