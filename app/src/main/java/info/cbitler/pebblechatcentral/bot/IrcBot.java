package info.cbitler.pebblechatcentral.bot;

import android.content.Context;
import android.content.Intent;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import info.cbitler.pebblechatcentral.intent.IRCIntent;

/**
 * Created by Chris on 3/5/2016.
 */
public class IrcBot extends PircBot {
    IRCIntent intent;
    public IrcBot(IRCIntent intent,String network, String[] channels, String nick, String pass) throws IrcException, IOException {
        this.setVerbose(true);
        this.setName(nick);
        this.setLogin("Test");
        this.connect(network);
        this.intent = intent;
        for(String channel : channels) {
            this.joinChannel(channel);
        }
        this.changeNick(nick);
        if(pass != null) {
            this.sendMessage("nickserv","identify " + nick + " " + pass);
        }
        Intent i = new Intent("CONNECT");
        i.putExtra("reason","Connected!");
        intent.sendBroadcast(i);
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        PebbleDictionary dict = new PebbleDictionary();
        dict.addString(0,channel);
        dict.addString(1,sender);
        dict.addString(2,message);
        if(message.toLowerCase().contains(this.getName().toLowerCase())) {
            dict.addString(3,"true");
        }
        PebbleKit.sendDataToPebble(intent.getApplicationContext(), UUID.fromString("22a0388d-94dc-4b47-a5d2-32121c87afa4"), dict);
    }
}
