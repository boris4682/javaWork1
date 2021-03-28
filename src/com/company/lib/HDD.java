package com.company.lib;

import java.io.FileWriter;
import java.io.IOException;

public class HDD extends RecordingDevice {
    public  HDD(){

    }

    public  HDD(String data[], FileWriter logger) throws IOException {
        this.setTypeDrive(data[0]);
        this.setName(data[1]);

        try {
            this.setVolume(Integer.parseInt(data[2]));
            this.setSpeed(Integer.parseInt(data[3]));
        } catch (NumberFormatException e) {
            logger.write("\n" + e.getClass().toString() + ": " + e.getMessage() + "\n");
        }
        this.setConnectionInterface(data[4]);
    }

}
