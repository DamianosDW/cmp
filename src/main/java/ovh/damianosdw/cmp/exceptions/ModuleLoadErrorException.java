/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.exceptions;

import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.modules.AppStatus;

public class ModuleLoadErrorException extends RuntimeException
{
    public ModuleLoadErrorException()
    {
        super();
    }

    public ModuleLoadErrorException(String message)
    {
        super(message);
        AppStatus.showAppStatus(AppStatusType.ERROR, "Nie udało się załadować modułu!");
    }
}
