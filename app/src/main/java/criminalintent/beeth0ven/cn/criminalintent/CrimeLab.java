package criminalintent.beeth0ven.cn.criminalintent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Air on 2017/1/1.
 */

public class CrimeLab {

    public static List<Crime> crimes = new ArrayList<>();

    public static Crime getCrime(UUID id) {
        for (Crime crime: crimes) {
            if (crime.id.equals(id)) { return crime; }
        }
        return null;
    }

    public static void deleteCrimeWithId(UUID id) {
        crimes.remove(getCrime(id));
    }
}
