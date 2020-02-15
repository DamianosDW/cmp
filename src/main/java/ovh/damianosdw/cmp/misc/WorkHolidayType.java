/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.misc;

import lombok.Getter;
import lombok.Setter;

public enum WorkHolidayType
{
    REST("Wypoczynkowy"),
    FREE("Bezpłatny"),
    MATERNITY_LEAVE("Macierzyński"),
    PATERNITY_LEAVE("Tacierzyński"),
    PATERNITY_LEAVE_SHORT("Ojcowski"),
    PARENTAL_LEAVE("Rodzicielski"),
    RAISING("Wychowawczy"),
    ON_DEMAND("Na żądanie"),
    OCCASIONAL("Okolicznościowy");


    @Setter @Getter
    private String type;

    WorkHolidayType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return type;
    }
}
