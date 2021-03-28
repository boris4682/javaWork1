package com.company.lib;

import java.io.Serializable;

public abstract class RecordingDevice implements Serializable {
    protected int volume;
    protected int speed;
    protected String connectionInterface;
    protected String name;

    protected String typeDrive;

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getConnectionInterface() {
        return connectionInterface;
    }
    public void setConnectionInterface(String connectionInterface) {
        this.connectionInterface = connectionInterface;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTypeDrive() {
        return typeDrive;
    }
    public void setTypeDrive(String speed) {
        this.typeDrive = speed;
    }

    @Override
    public String toString() {
        return "This device has type is " + this.getTypeDrive() + "; Name is " + this.getName() + "; Volume:" + this.getVolume() + "; Speed:" + this.getSpeed() + "; Connection interface: " + this.getConnectionInterface();
    }
}
