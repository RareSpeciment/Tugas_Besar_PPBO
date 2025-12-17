package id.komorebi.util;

import id.komorebi.dao.DAOFactory;
import id.komorebi.model.ActivityLog;
import id.komorebi.model.User;

public class Logger {
    public static void log(User user, String action, String details) {
        if (user == null) return;
        new Thread(() -> {
            try {
                ActivityLog log = new ActivityLog();
                log.setUser(user);
                log.setAction(action);
                log.setDetails(details);
                DAOFactory.createActivityLogDao().insert(log);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}