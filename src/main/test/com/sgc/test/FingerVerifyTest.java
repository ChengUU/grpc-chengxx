package com.sgc.test;

import com.sgc.FingerVerifyServer;

import java.io.IOException;

public class FingerVerifyTest {
    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        final FingerVerifyServer server = new FingerVerifyServer();
        server.start();
    }
}
