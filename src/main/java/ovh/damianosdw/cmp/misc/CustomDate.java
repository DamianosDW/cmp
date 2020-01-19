/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.misc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CustomDate extends Date
{
    public static final String DEFAULT_DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    private String format;

    public CustomDate()
    {
        format = DEFAULT_DATETIME_FORMAT;
    }

    public CustomDate(String format)
    {
        this.format = format;
    }

    public CustomDate(long date)
    {
        super(date);
        format = DEFAULT_DATETIME_FORMAT;
    }

    @Override
    public String toString()
    {
        LocalDateTime localDateTime = this.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }
}
