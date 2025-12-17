package id.komorebi.controller;

import id.komorebi.dao.ActivityLogDAO;
import id.komorebi.dao.DAOFactory;
import id.komorebi.model.ActivityLog;
import id.komorebi.util.UIHelper;

import java.util.List;
import java.util.Collections;

public class ActivityLogController {

    private final ActivityLogDAO logDAO;

    public ActivityLogController() {
        this.logDAO = DAOFactory.createActivityLogDao();
    }

    public List<ActivityLog> getAllLogs() {
        try {
            return logDAO.findAll();
        } catch (Exception e) {
            UIHelper.showError("Failed to load activity logs: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}