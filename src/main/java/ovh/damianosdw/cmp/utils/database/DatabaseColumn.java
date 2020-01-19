/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.utils.database;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DatabaseColumn
{
    private final String name;
    private final String type;
    private final boolean primaryKey;
    private final boolean notNull;
}
