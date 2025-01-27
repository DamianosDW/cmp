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
import ovh.damianosdw.cmp.misc.CustomDate;
import ovh.damianosdw.cmp.utils.database.persisters.CustomDatePersister;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "employees")
public class Employee
{
    @DatabaseField(columnName = "employee_id", unique = true, generatedId = true, canBeNull = false)
    private long employeeId;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;
    @DatabaseField(columnName = "surname", canBeNull = false)
    private String surname;
    @DatabaseField(columnName = "job_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private JobTitle jobTitle;
    @DatabaseField(columnName = "sex", canBeNull = false)
    private String sex;
    @DatabaseField(columnName = "salary", canBeNull = false)
    private BigDecimal salary;
    @DatabaseField(columnName = "work_start_date", canBeNull = false, format = "dd.MM.yyyy", persisterClass = CustomDatePersister.class)
    private CustomDate workStartDate;
    @DatabaseField(columnName = "contact", defaultValue = "-", canBeNull = false)
    private String contact;

    @Override
    public String toString() // used by TableView
    {
        return name + " " + surname;
    }
}
