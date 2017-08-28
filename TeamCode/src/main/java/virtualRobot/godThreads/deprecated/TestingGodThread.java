package virtualRobot.godThreads.deprecated;

import virtualRobot.GodThread;

/**
 * Created by shant on 1/15/2016.
 * Basic Test of GodThread
 */
public class TestingGodThread extends GodThread {
    @Override
    public void realRun() throws InterruptedException {
        //final AtomicBoolean redisLeft = new AtomicBoolean();







        //These two threads should be running from the beginning of the program to provide accurate data
        /*MonitorThread watchingForDebris = new DebrisMonitor();
        Thread dm = new Thread(watchingForDebris);
        dm.start();
        children.add(dm);

        MonitorThread watchingForTime = new TimeMonitor(System.currentTimeMillis(), 30000);
        Thread tm = new Thread(watchingForTime);
        tm.start();
        children.add(tm);


        // THIS IS THE STANDARD FORMAT FOR ADDING A LOGICTHREAD TO THE LIST
        LogicThread moveSlowly = new LogicThread() {
            @Override
            public void realRun() {
                runCommand(new Translate(5000, Translate.Direction.FORWARD, 0.3, 0));
            }
        };
        Thread mtb = new Thread(moveSlowly);
        mtb.start();
        children.add(mtb);
        */
        /*LogicThread cameraTest = new LogicThread() {
            @Override
            public void realRun() {
                runCommand (new TakePicture(redisLeft));
            }
        };
        Thread ct = new Thread(cameraTest);
        ct.start();
        children.add(ct);
        delegateMonitor(ct, new MonitorThread[]{});
        Log.d("cameraReturn", redisLeft.get() + " ");
        if (redisLeft.get()) {
            LogicThread pushLeftButton = new PushLeftButton();
            Thread plb = new Thread (pushLeftButton);
            plb.start();
            children.add(plb);
            delegateMonitor(plb, new MonitorThread[]{});
            Log.d("cameraReturn", "pushed left button");
        }
        else if (!redisLeft.get()) {
            LogicThread pushRightButton = new PushRightButton();
            Thread prb = new Thread (pushRightButton);
            prb.start();
            children.add(prb);
            delegateMonitor(prb, new MonitorThread[]{});
            Log.d("cameraReturn", "pushed right button");
        }

        //delegateMonitor(mtb, new MonitorThread[]{watchingForDebris, watchingForTime});*/

        /*LogicThread translateTest = new LogicThread() {
            @Override
            public void realRun() {

                runCommand (new Rotate (90));
                runCommand (new Rotate (0));
                runCommand (new Rotate (-90));
                runCommand (new Rotate (0));
            }
        };

        Thread t = new Thread(translateTest);
        t.start();
        children.add(t);
        delegateMonitor(t, new MonitorThread[]{});*/



    }
}
