/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.misc;

import lombok.Getter;

public enum Sex
{
    MALE("Mężczyzna"),
    FEMALE("Kobieta");

    @Getter
    private String sex;

    Sex(String sex)
    {
        this.sex = sex;
    }

    @Override
    public String toString()
    {
        return sex;
    }
}
