package info.cbitler.pebblechatcentral;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;

import org.jibble.pircbot.IrcException;

import java.io.IOException;

import info.cbitler.pebblechatcentral.bot.IrcBot;
import info.cbitler.pebblechatcentral.intent.IRCIntent;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BroadcastReceiver rc = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ((TextView)findViewById(R.id.textView5)).setText("Cannot connect - " + intent.getStringExtra("reason"));
            }
        };

        BroadcastReceiver rc2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ((TextView)findViewById(R.id.textView5)).setText("Connected!");
            }
        };

        registerReceiver(rc, new IntentFilter("CONNECT_F"));
        registerReceiver(rc2, new IntentFilter("CONNECT"));
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if(intent == null) {
            if (prefs.getString("network", null) != null && prefs.getString("channels", null) != null && prefs.getString("nick", null) != null) {
                intent = new Intent(Intent.ACTION_ALL_APPS, null, this, IRCIntent.class);
                intent.putExtra("network", prefs.getString("network", null));
                intent.putExtra("channels", prefs.getString("channels", null).split(","));
                intent.putExtra("nick", prefs.getString("nick", null));
                intent.putExtra("pass", prefs.getString("pass", null));
                ((EditText) findViewById(R.id.editText)).setText(prefs.getString("network", null));
                ((EditText) findViewById(R.id.editText2)).setText(prefs.getString("channels", null));
                ((EditText) findViewById(R.id.editText4)).setText(prefs.getString("nick", null));
                ((EditText) findViewById(R.id.editText5)).setText(prefs.getString("pass", null));
                startService(intent);
                ((TextView) findViewById(R.id.textView5)).setText("Connecting!");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void save(View view) {
        intent = new Intent(Intent.ACTION_ALL_APPS,null,this, IRCIntent.class);
        intent.putExtra("network", ((EditText) findViewById(R.id.editText)).getText().toString());
        intent.putExtra("channels", ((EditText) findViewById(R.id.editText2)).getText().toString().split(","));
        intent.putExtra("nick", ((EditText) findViewById(R.id.editText4)).getText().toString());
        if(((EditText)findViewById(R.id.editText5)).getText().length() != 0) {
            intent.putExtra("pass",((EditText)findViewById(R.id.editText5)).getText().toString());
        }
        SharedPreferences.Editor edit = getPreferences(MODE_PRIVATE).edit();
        edit.remove("pass");
        edit.remove("network");
        edit.remove("channels");
        edit.remove("nick");
        edit.putString("network",((EditText) findViewById(R.id.editText)).getText().toString());
        edit.putString("channels",((EditText) findViewById(R.id.editText2)).getText().toString());
        edit.putString("nick",((EditText) findViewById(R.id.editText4)).getText().toString());
        edit.putString("pass",((EditText) findViewById(R.id.editText5)).getText().toString());
        edit.commit();

        startService(intent);
        ((TextView)findViewById(R.id.textView5)).setText("Saved/Connecting!");
    }
    @Override
    public void onResume() {
        super.onResume();
        boolean isConnected = PebbleKit.isWatchConnected(this);
    }
}
