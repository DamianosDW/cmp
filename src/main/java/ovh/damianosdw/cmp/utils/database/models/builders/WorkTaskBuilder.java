/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils.database.models.builders;

import ovh.damianosdw.cmp.misc.CustomDate;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.WorkTask;

public class WorkTaskBuilder
{
    private long taskId;
    private String name;
    private String status;
    private Employee creator;
    private Employee assignedTo;
    private CustomDate creationDate;
    private CustomDate updateDate;
    private String additionalInfo;

    public static WorkTaskBuilder builder()
    {
        return new WorkTaskBuilder();
    }

    public WorkTaskBuilder taskId(long taskId)
    {
        this.taskId = taskId;
        return this;
    }

    public WorkTaskBuilder name(String name)
    {
        this.name = name;
        return this;
    }

    public WorkTaskBuilder status(String status)
    {
        this.status = status;
        return this;
    }

    public WorkTaskBuilder creator(Employee creator)
    {
        this.creator = creator;
        return this;
    }

    public WorkTaskBuilder assignedTo(Employee assignedTo)
    {
        this.assignedTo = assignedTo;
        return this;
    }

    public WorkTaskBuilder creationDate(CustomDate creationDate)
    {
        this.creationDate = creationDate;
        return this;
    }

    public WorkTaskBuilder updateDate(CustomDate updateDate)
    {
        this.updateDate = updateDate;
        return this;
    }

    public WorkTaskBuilder additionalInfo(String additionalInfo)
    {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public WorkTask build()
    {
        return new WorkTask(taskId, name, status, creator, assignedTo, creationDate, updateDate, additionalInfo);
    }
}
