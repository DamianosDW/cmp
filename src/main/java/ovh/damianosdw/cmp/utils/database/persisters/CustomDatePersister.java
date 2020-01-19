package ovh.damianosdw.cmp.utils.database.persisters;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateType;
import lombok.Getter;
import ovh.damianosdw.cmp.misc.CustomDate;

import java.sql.Timestamp;

public class CustomDatePersister extends DateType
{
    @Getter
    private static final CustomDatePersister singleton = new CustomDatePersister();

    private CustomDatePersister()
    {
        super(SqlType.DATE, new Class<?>[] { CustomDate.class });
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        CustomDate date = (CustomDate) javaObject;

        if(date == null)
            return null;
        else
            return new Timestamp(date.getTime());
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos)
    {
        Timestamp value = (Timestamp)sqlArg;
        return new CustomDate(value.getTime());
    }
}
