package info.cbitler.pebblechatcentral.intent;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;

import info.cbitler.pebblechatcentral.MainActivity;
import info.cbitler.pebblechatcentral.bot.IrcBot;

/**
 * Created by Chris on 3/5/2016.
 */
public class IRCIntent extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    Handler handler;
    public IRCIntent(String name) {
        super(name);
    }

    public IRCIntent() {
        super("IRCIntent");
        handler = new Handler(Looper.getMainLooper());
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            IrcBot bot = new IrcBot(this);
        } catch (IrcException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            bot.setVerbose(true);
            bot.connect("irc.freenode.net");
            bot.changeNick("VoidWhisperer|PebbleTest");
            bot.joinChannel("#jflory7");
            bot.sendMessage("#jflory7", "Test");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), "Test2", Toast.LENGTH_LONG);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
}
