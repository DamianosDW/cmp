/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.misc;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum WorkTaskStatus
{
    NEW("NOWE"),
    ASSIGNED("PRZYPISANE"),
    COMPLETED("ZROBIONE"),
    FIX("DO POPRAWY");

    @Getter
    private String status;

    WorkTaskStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return status;
    }

    public static WorkTaskStatus getWorkTaskStatus(String status)
    {
        List<WorkTaskStatus> enums = getAllWorkTaskStatuses();

        for(WorkTaskStatus s : enums)
        {
            if(s.getStatus().equalsIgnoreCase(status))
                return s;
        }
        return null;
    }

    public static List<WorkTaskStatus> getAllWorkTaskStatuses()
    {
        return Arrays.asList(NEW, ASSIGNED, COMPLETED, FIX);
    }
}
