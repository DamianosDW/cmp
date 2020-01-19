/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils.database.models.builders;

import ovh.damianosdw.cmp.utils.database.models.JobTitle;

public class JobTitleBuilder
{
    private long jobId;
    private String name;
    private String salaryRange;
    private String responsibilities;

    public static JobTitleBuilder builder()
    {
        return new JobTitleBuilder();
    }

    public JobTitleBuilder jobId(long jobId)
    {
        this.jobId = jobId;
        return this;
    }

    public JobTitleBuilder name(String name)
    {
        this.name = name;
        return this;
    }

    public JobTitleBuilder salaryRange(String salaryRange)
    {
        this.salaryRange = salaryRange;
        return this;
    }

    public JobTitleBuilder responsibilities(String responsibilities)
    {
        this.responsibilities = responsibilities;
        return this;
    }

    public JobTitle build()
    {
        return new JobTitle(jobId, name, salaryRange, responsibilities);
    }
}
