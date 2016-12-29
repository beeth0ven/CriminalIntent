package criminalintent.beeth0ven.cn.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Air on 2016/12/28.
 */

public class Crime {
    public UUID id;
    public String title;
    public Date date;
    public boolean isSolved;

    public Crime() {
        id = UUID.randomUUID();
        date = new Date();
    }
}
