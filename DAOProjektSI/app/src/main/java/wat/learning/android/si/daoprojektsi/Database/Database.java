package wat.learning.android.si.daoprojektsi.Database;

import java.sql.SQLException;

/**
 * Created by Piotr on 27.03.2018.
 */

public interface Database {
    void UserLogin(String email, String seed, String password);
    void UserSeedCheck(String email, String seed);
    void UserPasswordReset(int lokatorId, String newPassword);
    //void UserLogout(int lokatorId);
    void GetMediaFromDb();
    void pobierzMiesięczneOpłaty(String miesiac, String rok);
}
