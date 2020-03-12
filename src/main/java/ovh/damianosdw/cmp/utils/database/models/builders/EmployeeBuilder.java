/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils.database.models.builders;

import ovh.damianosdw.cmp.misc.CustomDate;
import ovh.damianosdw.cmp.utils.database.models.Employee;
import ovh.damianosdw.cmp.utils.database.models.JobTitle;

import java.math.BigDecimal;

public class EmployeeBuilder
{
    private long employeeId;
    private String name;
    private String surname;
    private JobTitle jobTitle;
    private String sex;
    private CustomDate workStartDate;
    private BigDecimal salary;
    private String contact;

    public static EmployeeBuilder builder()
    {
        return new EmployeeBuilder();
    }

    public EmployeeBuilder employeeId(long employeeId)
    {
        this.employeeId = employeeId;
        return this;
    }

    public EmployeeBuilder name(String name)
    {
        this.name = name;
        return this;
    }

    public EmployeeBuilder surname(String surname)
    {
        this.surname = surname;
        return this;
    }

    public EmployeeBuilder jobTitle(JobTitle jobTitle)
    {
        this.jobTitle = jobTitle;
        return this;
    }

    public EmployeeBuilder sex(String sex)
    {
        this.sex = sex;
        return this;
    }

    public EmployeeBuilder salary(BigDecimal salary)
    {
        this.salary = salary;
        return this;
    }

    public EmployeeBuilder workStartDate(CustomDate workStartDate)
    {
        this.workStartDate = workStartDate;
        return this;
    }

    public EmployeeBuilder contact(String contact)
    {
        this.contact = contact;
        return this;
    }

    public Employee build()
    {
        return new Employee(employeeId, name, surname, jobTitle, sex, salary, workStartDate, contact);
    }
}
