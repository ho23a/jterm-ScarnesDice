package com.ho23a.scarnesdice;

public class PlayerState {
    private String id;
    private String email;
    private PlayerStatus status;

    public PlayerState() {}

    public PlayerState(String email) {
        this.email = email;
        status = PlayerStatus.READY;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }
}

enum PlayerStatus {
    READY,
    IN_GAME
}