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

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    //-----------------------------------------------------------------------//

    @Override
    public final void init()
    {
        setSize(screenW, screenH);

        screenW = getWidth();
        screenH = getHeight();

        off_screen = createImage(screenW, screenH);
        off_screen_g = off_screen.getGraphics();

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        setFocusable(true);
        requestFocus();
        requestFocusInWindow();

        initialize();

        t = new Thread(this);
        t.start();
    }

    //-----------------------------------------------------------------------//

    @Override
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
            catch (Exception x)
            {
                x.printStackTrace();
            }
        }
    }

    //-----------------------------------------------------------------------//

    @Override
    public void update(Graphics g)
    {
        if (off_screen == null ||
            off_screen_g == null ||
            screenW != getWidth() ||
            screenH != getHeight())
        {
            screenW = getWidth();
            screenH = getHeight();

            off_screen = createImage(screenW, screenH);
            off_screen_g = off_screen.getGraphics();
        }

        off_screen_g.setColor(Color.BLACK);
        off_screen_g.fillRect(0, 0, screenW, screenH);

        paint(off_screen_g);

        g.drawImage(off_screen, 0, 0, null);
    }

    //-----------------------------------------------------------------------//

    @Override
    public final void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();

        if (code >= 0 && code < pressing.length)
        {
            pressing[code] = true;
        }
    }

    //-----------------------------------------------------------------------//

    @Override
    public final void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode();

        if (code >= 0 && code < pressing.length)
        {
            pressing[code] = false;
        }
    }

    //-----------------------------------------------------------------------//

    @Override
    public void keyTyped(KeyEvent e) {}

    //-----------------------------------------------------------------------//
}