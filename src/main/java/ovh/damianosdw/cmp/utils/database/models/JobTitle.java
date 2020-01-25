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

@Data
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "job_titles")
public class JobTitle
{
    @DatabaseField(columnName = "job_id", unique = true, generatedId = true, canBeNull = false)
    private long jobId;
    @DatabaseField(columnName = "name", unique = true, canBeNull = false)
    private String name;
    @DatabaseField(columnName = "salary_range", canBeNull = false)
    private String salaryRange;
    @DatabaseField(columnName = "responsibilities", canBeNull = false, dataType = DataType.LONG_STRING)
    private String responsibilities;

    @Override
    public String toString() // used by Combobox
    {
        return name;
    }
}
