/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.exceptions;

import ovh.damianosdw.cmp.misc.AppStatusType;
import ovh.damianosdw.cmp.modules.AppStatus;

public class InvalidDataException extends RuntimeException
{
    public InvalidDataException()
    {
        super();
    }

    public InvalidDataException(String message)
    {
        super(message);
        AppStatus.showAppStatus(AppStatusType.ERROR, "Nieprawidłowe dane!");
    }
}
