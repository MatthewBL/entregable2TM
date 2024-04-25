package us.master.entregable2.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserFunctionalities {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Date obtainCreatedDate(User user) {
        try {
            return sdf.parse(user.getCreated());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
