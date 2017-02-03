package criminalintent.beeth0ven.cn.criminalintent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Air on 2017/1/1.
 */

public class CrimeLab {

    public static RealmResults<Crime> crimes = getCrimes();

    private static RealmResults<Crime> getCrimes() {
        return Realm.getDefaultInstance()
                .where(Crime.class)
                .findAll();
    }

    public static Crime getCrime(long id) {
        return Realm.getDefaultInstance()
                .where(Crime.class)
                .equalTo("id", id)
                .findFirst();
    }

    public static void deleteCrimeWithId(long id) {
        Realm.getDefaultInstance().executeTransaction(realm -> getCrime(id).deleteFromRealm());
    }

}
