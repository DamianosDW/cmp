/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.misc;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum WorkTaskPriority
{
    LOW("Niski"),
    NORMAL("Normalny"),
    HIGH("Wysoki"),
    CRITICAL("Krytyczny");

    @Getter
    private String priority;

    WorkTaskPriority(String priority)
    {
        this.priority = priority;
    }

    @Override
    public String toString()
    {
        return priority;
    }

    public static WorkTaskPriority getWorkTaskPriorityValue(String status)
    {
        List<WorkTaskPriority> enums = getAllWorkTaskPriorities();

        for(WorkTaskPriority p : enums)
        {
            if(p.getPriority().equalsIgnoreCase(status))
                return p;
        }
        return null;
    }

    public static List<WorkTaskPriority> getAllWorkTaskPriorities()
    {
        return Arrays.asList(LOW, NORMAL, HIGH, CRITICAL);
    }
}
