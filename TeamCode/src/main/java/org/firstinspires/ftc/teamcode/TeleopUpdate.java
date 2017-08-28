package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import virtualRobot.godThreads.deprecated.TeleopGodThread;

/**
 * _____ ______   ___  ___  ________
 * |\   _ \  _   \|\  \|\  \|\   ____\             .-""-.
 * \ \  \\\__\ \  \ \  \\\  \ \  \___|_           /[] _ _\
 * \ \  \\|__| \  \ \   __  \ \_____  \         _|_o_LII|_
 * \ \  \    \ \  \ \  \ \  \|____|\  \       / | ==== | \
 * \ \__\    \ \__\ \__\ \__\____\_\  \      |_| ==== |_|
 * \|__|     \|__|\|__|\|__|\_________\      ||" ||  ||
 * \|_________|      ||LI  o ||
 * ||'----'||
 * /__|    |__\
 * <p>
 * Created by shant on 11/27/2015.
 * The Update Thread for Teleop
 */
@TeleOp(name = "Testing: teleop", group = "Testing")
public class TeleopUpdate extends UpdateThread {
    @Override
    public void setGodThread() {
        godThread = TeleopGodThread.class;
    }

    @Override
    public void addPresets() {

    }

}
