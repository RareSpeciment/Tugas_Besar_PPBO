package id.komorebi.model;

import java.time.LocalDateTime;

public class ActivityLog {
    private int logId;
    private User user;
    private String action;
    private String details;
    private LocalDateTime createdAt;

    public ActivityLog() {
        this.createdAt = LocalDateTime.now();
    }

    public ActivityLog(int logId, User user, String action, String details, LocalDateTime createdAt) {
        this.logId = logId;
        this.user = user;
        this.action = action;
        this.details = details;
        this.createdAt = createdAt;
    }

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "ActivityLog{" +
                "logId=" + logId +
                ", user=" + (user != null ? user.getUsername() : null) +
                ", action='" + action + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
