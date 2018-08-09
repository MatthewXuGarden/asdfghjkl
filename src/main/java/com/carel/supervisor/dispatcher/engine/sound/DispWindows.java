package com.carel.supervisor.dispatcher.engine.sound;

import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class DispWindows extends JFrame implements ActionListener
{
    private JLabel luser = null;
    private JLabel lpass = null;
    private JLabel lnum = null;
    private JTextField tuser = null;
    private JTextField tnumber = null;
    private JPasswordField tpass = null;
    private JButton button = null;
    private JPanel up = null;
    private JPanel dw = null;
    private String sound = "";
    private boolean playsound = false;
    AudioClip ac = null;

    public DispWindows(String sound, String l1, String l2, String l3, String l4)
    {
        super("Alarm Window");

        if ((sound != null) && (sound.trim().length() > 0))
        {
            this.sound = sound;
            this.playsound = true;
        }

        this.setSize(340, 150);
        this.setLocation(100, 100);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(new BorderLayout(10, 10));

        this.up = new JPanel();
        this.up.setLayout(new GridLayout(3, 1, 2, 2));

        this.dw = new JPanel();
        this.dw.setLayout(new FlowLayout(FlowLayout.CENTER));

        luser = new JLabel(l1 + ":");
        lpass = new JLabel(l2 + ":");
        lnum = new JLabel(l3 + ":");
        tuser = new JTextField(15);
        tpass = new JPasswordField(15);
        tnumber = new JTextField(3);
        tnumber.setEditable(false);

        this.up.add(luser);
        this.up.add(tuser);
        this.up.add(lpass);
        this.up.add(tpass);
        this.up.add(lnum);
        this.up.add(tnumber);

        button = new JButton(l4);
        button.addActionListener(this);

        this.dw.add(button);

        c.add(this.up, BorderLayout.NORTH);
        c.add(this.dw, BorderLayout.SOUTH);
    }

    public void dw_display()
    {
    	
        this.setVisible(true);   

        if (this.playsound)
        {
            try
            {
                URL u = new URL("file:///" + this.sound);
                this.ac = Applet.newAudioClip(u);
                this.ac.loop();
            }
            catch (Exception e)
            {
                Object[] obj = { this.sound };
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Start",
                    EventDictionary.TYPE_INFO, "D014", obj);
            }
        }
    }

    public void dw_close()
    {
        if (this.ac != null)
        {
            this.ac.stop();
        }

        this.dispose();
    }

    public void dw_setNumAl(int number)
    {
        if (this.tnumber != null)
        {
            try
            {
                this.tnumber.setText(String.valueOf(number));
            }
            catch (RuntimeException e)
            {
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof JButton)
        {
            DispWinAlarm.getInstance().checkUserAck(this.tuser.getText(),
                new String(this.tpass.getPassword()));
        }
    }
}
