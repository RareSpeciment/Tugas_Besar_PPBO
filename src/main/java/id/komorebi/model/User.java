package id.komorebi.model;

import java.time.LocalDateTime;

import id.komorebi.model.enums.UserStatus;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String fullname;
    private Role role;
    private UserStatus status;
    private LocalDateTime deactivatedAt;
    private LocalDateTime createdAt;

    public User() {
        this.status = UserStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public User(int userId, String username, String passwordHash, String fullname, Role role,
                UserStatus status, LocalDateTime deactivatedAt, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullname = fullname;
        this.role = role;
        this.status = status;
        this.deactivatedAt = deactivatedAt;
        this.createdAt = createdAt;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public LocalDateTime getDeactivatedAt() { return deactivatedAt; }
    public void setDeactivatedAt(LocalDateTime deactivatedAt) { this.deactivatedAt = deactivatedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role=" + (role != null ? role.getRoleName() : null) +
                '}';
    }



    // @Override
    // public String toString() {
    //     return "User{" +
    //             "userId=" + userId +
    //             ", username='" + username + '\'' +
    //             ", fullname='" + fullname + '\'' +
    //             ", role=" + (role != null ? role.getRoleName() : null) +
    //             ", status=" + status +
    //             ", createdAt=" + createdAt +
    //             '}';
    // }
}
