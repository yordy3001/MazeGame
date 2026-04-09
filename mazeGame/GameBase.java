package mazeGame;

//---------------------------------------------------------------------------//

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

//---------------------------------------------------------------------------//

public abstract class GameBase extends Applet
                               implements Runnable,
                                          MouseListener,
                                          MouseMotionListener,
                                          KeyListener,
                                          KeyCodes
{
    //-----------------------------------------------------------------------//

    Image off_screen;
    Graphics off_screen_g;

    //-----------------------------------------------------------------------//

    boolean[] pressing = new boolean[1024];

    //-----------------------------------------------------------------------//

    int mx;
    int my;

    //-----------------------------------------------------------------------//

    Thread t;

    //-----------------------------------------------------------------------//

    int screenW = 1280;
    int screenH = 720;

    //-----------------------------------------------------------------------//

    public abstract void initialize();

    public abstract void inGameLoop();

    //-----------------------------------------------------------------------//

    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    //-----------------------------------------------------------------------//

    public final void init()
    {
        setSize(screenW, screenH);

        screenW = getWidth();
        screenH = getHeight();

        off_screen = createImage(screenW, screenH);
        off_screen_g = off_screen.getGraphics();

        initialize();

        requestFocus();

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        t = new Thread(this);
        t.start();
    }

    //-----------------------------------------------------------------------//

    public final void run()
    {
        while (true)
        {
            inGameLoop();
            repaint();

            try
            {
                Thread.sleep(16);
            }
            catch (Exception x) {}
        }
    }

    //-----------------------------------------------------------------------//

    public final void update(Graphics g)
    {
        if (off_screen == null || screenW != getWidth() || screenH != getHeight())
        {
            screenW = getWidth();
            screenH = getHeight();

            off_screen = createImage(screenW, screenH);
            off_screen_g = off_screen.getGraphics();
        }

        off_screen_g.setColor(Color.black);
        off_screen_g.fillRect(0, 0, screenW, screenH);

        paint(off_screen_g);

        g.drawImage(off_screen, 0, 0, null);
    }

    //-----------------------------------------------------------------------//

    public final void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();

        if (code >= 0 && code < pressing.length)
        {
            pressing[code] = true;
        }
    }

    //-----------------------------------------------------------------------//

    public final void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode();

        if (code >= 0 && code < pressing.length)
        {
            pressing[code] = false;
        }
    }

    //-----------------------------------------------------------------------//

    public void keyTyped(KeyEvent e) {}

    //-----------------------------------------------------------------------//
}