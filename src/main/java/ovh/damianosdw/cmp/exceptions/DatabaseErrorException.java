/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.exceptions;

import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.modules.AppStatus;

public class DatabaseErrorException extends RuntimeException
{
    public DatabaseErrorException()
    {
        super();
    }

    public DatabaseErrorException(String message)
    {
        super(message);
        AppStatus.showAppStatus(AppStatusType.ERROR, "Wystąpił problem z bazą danych!");
    }
}
