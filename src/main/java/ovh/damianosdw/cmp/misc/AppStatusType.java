/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.misc;

import lombok.Getter;
import lombok.Setter;

public enum AppStatusType
{
    OK("OK"),
    WARNING("WARNING"),
    ERROR("ERROR"),
    UNDEFINED("UNDEFINED"),
    DEBUG("DEBUG");

    @Setter @Getter
    private String type;

    AppStatusType(String type)
    {
        this.type = type;
    }
}
