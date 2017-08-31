package br.com.daciosoftware.pongv1;

import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;

import br.com.daciosoftware.simplegameenginev1.SGActivity;
import br.com.daciosoftware.simplegameenginev1.SGPreferences;

/**
 * Created by fdacio on 17/08/17.
 */
public class GameActivity extends SGActivity{

    public static final String TAG = "PongV1";
    private GameView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        enabledFullScreen();
        enabledKeepScreenOn();

        mView = new GameView(this);
        setContentView(mView);

        SGPreferences preferences = getPreferences();

        if(preferences.getInt("first_time",-1)==-1){
            preferences.begin()
                    .putInt("first_time", 1)
                    .putInt("difficult", 0)
                    .putInt("high_score", 15)
                    .end();
            Log.d(TAG, "Primeira inicialização");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Nível de Dificuldade: ");
        stringBuilder.append(preferences.getInt("difficult", 0));
        Log.d(TAG, stringBuilder.toString());

        stringBuilder.setLength(0);

        stringBuilder.append("High Score: ");
        stringBuilder.append(preferences.getInt("high_score", 0));
        Log.d(TAG, stringBuilder.toString());

        stringBuilder.setLength(0);
        preferences.begin().putFloat("time",1000*60).end();
        stringBuilder.append("Time: ");
        stringBuilder.append(preferences.getFloat("time",2000*60));
        Log.d(TAG, stringBuilder.toString());


        //setOrientation(SGOrientatio.LANDSCAPE);


    }


}
