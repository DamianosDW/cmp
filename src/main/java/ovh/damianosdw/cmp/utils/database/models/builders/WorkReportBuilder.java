/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils.database.models.builders;

import ovh.damianosdw.cmp.misc.CustomDate;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.WorkReport;

public class WorkReportBuilder
{
    private long reportId;
    private CustomDate sentDate;
    private Employee employee;
    private String workReport;

    public static WorkReportBuilder builder()
    {
        return new WorkReportBuilder();
    }

    public WorkReportBuilder reportId(long reportId)
    {
        this.reportId = reportId;
        return this;
    }

    public WorkReportBuilder sentDate(CustomDate sentDate)
    {
        this.sentDate = sentDate;
        return this;
    }

    public WorkReportBuilder employee(Employee employee)
    {
        this.employee = employee;
        return this;
    }

    public WorkReportBuilder workReport(String workReport)
    {
        this.workReport = workReport;
        return this;
    }

    public WorkReport build()
    {
        return new WorkReport(reportId, sentDate, employee, workReport);
    }
}
