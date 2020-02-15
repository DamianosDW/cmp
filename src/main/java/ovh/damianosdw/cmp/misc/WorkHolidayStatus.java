/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.misc;

import lombok.Getter;
import lombok.Setter;

public enum WorkHolidayStatus
{
    ACCEPTED("ZAAKCEPTOWANY"),
    SENT("WYSŁANE"),
    DECLINED("ODRZUCONY");

    @Setter @Getter
    private String status;

    WorkHolidayStatus(String status)
    {
        this.status = status;
    }
}
