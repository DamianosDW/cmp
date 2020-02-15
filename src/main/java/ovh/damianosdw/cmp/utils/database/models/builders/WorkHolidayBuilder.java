/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils.database.models.builders;

import ovh.damianosdw.cmp.misc.CustomDate;
import ovh.damianosdw.cmp.misc.WorkHolidayStatus;
import ovh.damianosdw.cmp.misc.WorkHolidayType;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.WorkHoliday;

public class WorkHolidayBuilder
{
    private long holidayId;
    private Employee employee;
    private CustomDate startDate;
    private CustomDate endDate;
    private String type;
    private String reason;
    private String status;

    public static WorkHolidayBuilder builder()
    {
        return new WorkHolidayBuilder();
    }

    public WorkHolidayBuilder holidayId(long holidayId)
    {
        this.holidayId = holidayId;
        return this;
    }

    public WorkHolidayBuilder employee(Employee employee)
    {
        this.employee = employee;
        return this;
    }

    public WorkHolidayBuilder startDate(CustomDate startDate)
    {
        this.startDate = startDate;
        return this;
    }

    public WorkHolidayBuilder endDate(CustomDate endDate)
    {
        this.endDate = endDate;
        return this;
    }

    public WorkHolidayBuilder type(WorkHolidayType type)
    {
        this.type = type.getType();
        return this;
    }

    public WorkHolidayBuilder reason(String reason)
    {
        this.reason = reason;
        return this;
    }

    public WorkHolidayBuilder status(WorkHolidayStatus status)
    {
        this.status = status.getStatus();
        return this;
    }

    public WorkHoliday build()
    {
        return new WorkHoliday(holidayId, employee, startDate, endDate, type, reason, status);
    }
}
