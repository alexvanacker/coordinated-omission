package com.avanacker.coom.app;

import static spark.Spark.*;
import static spark.Spark.stop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

/**
 * A simple REST API App that pauses for some time at a given interval.
 *
 * Created by alexvanacker on 17/08/17.
 */
public class SampleApp {

    private final static Logger logger = LoggerFactory.getLogger(SampleApp.class);

    private int pauseTimeS = 15;

    private int pauseTimeIntervalMS = 60000;

    private long lastPauseTime = System.currentTimeMillis();

    private String[] words = {"a", "coucou", "help", "load", "coordinated", "omission", "jmeter", "gatling", "babies", "blog",
                                "fame", "fortune", "coffee", "need"};

    public SampleApp(){
        this.lastPauseTime = System.currentTimeMillis();
    }

    public SampleApp(int pauseTimeS, int pauseTimeIntervalMS){
        this.pauseTimeS = pauseTimeS;
        this.pauseTimeIntervalMS = pauseTimeIntervalMS;
        this.lastPauseTime = System.currentTimeMillis();
    }

    /**
     * Return a random word from words.
     * @return
     */
    public String getWord(){
        logger.info("Getting word...");
        int randomInt = (int)Math.round(Math.random() * (words.length - 1));
        logger.info("Return word");
        return words[randomInt];
    }

    /**
     * Start app
     */
    public void run(){
        get("/", (req, res) -> {
            logger.info("Got request");
            long currentTime = System.currentTimeMillis();
            // Pause the system
            if(currentTime - this.lastPauseTime >= this.pauseTimeIntervalMS){
                logger.info("Pausing system for {} seconds", pauseTimeS);
                Thread.sleep(pauseTimeS * 1000);
                logger.info("And we're back!");
            }
            return getWord();
        });


        get("/stop", (req, res) -> {
            stop();
            return "Server stopped.";
        });
    }

    public void stop(){
        Spark.stop();
    }

    public static void main(String[] args){
        SampleApp app = new SampleApp();
        app.run();
    }
}
