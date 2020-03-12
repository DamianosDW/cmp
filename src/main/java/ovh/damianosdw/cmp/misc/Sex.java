/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.misc;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

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

    public static Sex getSexValue(String sex)
    {
        List<Sex> enums = Arrays.asList(MALE, FEMALE);

        for(Sex s : enums)
        {
            if(s.getSex().equalsIgnoreCase(sex))
                return s;
        }
        return null;
    }
}
