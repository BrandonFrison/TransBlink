package com.example.brandon.transblink;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static android.os.Build.VERSION_CODES.M;
import static java.security.AccessController.getContext;


public class ThemeChange extends AppCompatActivity {


    private int themeSel;
    private int fontSel;
    private int colourSel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeChange themeChg = new ThemeChange();
        themeSel = themeChg.findTheme(this);
        fontSel = themeChg.findFont(this);
        colourSel = themeChg.findColour(this);
        setTheme(themeSel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_change);

        RadioButton defFont = (RadioButton) findViewById(R.id.radioButtonDefaultFont);
        RadioButton typewriterFont = (RadioButton) findViewById(R.id.radioButtonTypewriter);
        RadioButton condensedFont = (RadioButton) findViewById(R.id.radioButtonCondensed);
        RadioButton notoFont = (RadioButton) findViewById(R.id.radioButtonNotoSerif);
        RadioButton cursiveFont = (RadioButton) findViewById(R.id.radioButtonCursive);
        RadioButton carroisFont = (RadioButton) findViewById(R.id.radioButtonCarroisGothic);
        RadioButton droidFont = (RadioButton) findViewById(R.id.radioButtonDroidSans);
        RadioButton devFont = (RadioButton) findViewById(R.id.radioButtonBrandonFont);

        switch(fontSel)
        {
            case 1:
                defFont.setChecked(true);
                break;
            case 2:
                typewriterFont.setChecked(true);
                break;
            case 3:
                condensedFont.setChecked(true);
                break;
            case 4:
                notoFont.setChecked(true);
                break;
            case 5:
                cursiveFont.setChecked(true);
                break;
            case 6:
                carroisFont.setChecked(true);
                break;
            case 7:
                droidFont.setChecked(true);
                break;
            case 8:
                devFont.setChecked(true);
                break;
            default:
                defFont.setChecked(true);
                break;
        }


        RadioButton def = (RadioButton) findViewById(R.id.radioButtonDefaultTheme);
        RadioButton dark= (RadioButton) findViewById(R.id.radioButtonDarkTheme);
        RadioButton light = (RadioButton) findViewById(R.id.radioButtonLightTheme);
        RadioButton crazy = (RadioButton) findViewById(R.id.radioButtonCrazyTheme);
        RadioButton devfav = (RadioButton) findViewById(R.id.radioButtonDevTheme);
        RadioButton royalty = (RadioButton) findViewById(R.id.radioButtonRoyaltyTheme);

        switch(colourSel)
        {
            case 1:
                def.setChecked(true);
                break;
            case 2:
                dark.setChecked(true);
                break;
            case 3:
                light.setChecked(true);
                break;
            case 4:
                crazy.setChecked(true);
                break;
            case 5:
                devfav.setChecked(true);
                break;
            case 6:
                royalty.setChecked(true);
                break;
            default:
                def.setChecked(true);
                break;
        }

        Button theme = (Button) findViewById(R.id.buttonChangeTheme);

        theme.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                themeChg();
            }
        }


        );

    }

    public void themeChg()
    {

        RadioButton defFont = (RadioButton) findViewById(R.id.radioButtonDefaultFont);
        RadioButton typewriterFont = (RadioButton) findViewById(R.id.radioButtonTypewriter);
        RadioButton condensedFont = (RadioButton) findViewById(R.id.radioButtonCondensed);
        RadioButton notoFont = (RadioButton) findViewById(R.id.radioButtonNotoSerif);
        RadioButton cursiveFont = (RadioButton) findViewById(R.id.radioButtonCursive);
        RadioButton carroisFont = (RadioButton) findViewById(R.id.radioButtonCarroisGothic);
        RadioButton droidFont = (RadioButton) findViewById(R.id.radioButtonDroidSans);
        RadioButton devFont = (RadioButton) findViewById(R.id.radioButtonBrandonFont);

        RadioButton def = (RadioButton) findViewById(R.id.radioButtonDefaultTheme);
        RadioButton dark= (RadioButton) findViewById(R.id.radioButtonDarkTheme);
        RadioButton light = (RadioButton) findViewById(R.id.radioButtonLightTheme);
        RadioButton crazy = (RadioButton) findViewById(R.id.radioButtonCrazyTheme);
        RadioButton devfav = (RadioButton) findViewById(R.id.radioButtonDevTheme);
        RadioButton royalty = (RadioButton) findViewById(R.id.radioButtonRoyaltyTheme);
        String hold = "";
        String font = "";

        if(def.isChecked())
        {
            hold = "1";
        }
        if(dark.isChecked())
        {
            hold = "2";
        }
        if(light.isChecked())
        {
            hold = "3";
        }
        if(crazy.isChecked())
        {
            hold = "4";
        }
        if(devfav.isChecked())
        {
            hold = "5";
        }
        if(royalty.isChecked())
        {
            hold = "6";
        }
        if(defFont.isChecked())
        {
            font = "1";
        }
        if(typewriterFont.isChecked())
        {
            font = "2";
        }
        if(condensedFont.isChecked())
        {
            font = "3";
        }
        if(notoFont.isChecked())
        {
            font = "4";
        }
        if(cursiveFont.isChecked())
        {
            font = "5";
        }
        if(carroisFont.isChecked())
        {
            font = "6";
        }
        if(droidFont.isChecked())
        {
            font = "7";
        }
        if(devFont.isChecked())
        {
            font = "8";
        }

        try
        {
                OutputStreamWriter outFile = new OutputStreamWriter(openFileOutput("theme.txt", Context.MODE_PRIVATE));
                outFile.write(hold + "," + font);
                outFile.close();
        }
        catch (Exception ex)
        {

        }

        Intent intent = new Intent(ThemeChange.this, MainActivity.class);
        startActivity(intent);


    }




    public int findFont(Context con){
        String[] choice = new String[2];
        String Filereadline = "";

        try
        {

            InputStream inFile = con.openFileInput("theme.txt");
            InputStreamReader isr = new InputStreamReader(inFile);
            BufferedReader br = new BufferedReader(isr);
            Filereadline = br.readLine();
            if(Filereadline.equals(""))
            {
                choice[0] = "1";
                choice[1] = "1";

            }
            else {
                choice = Filereadline.split(",");
            }
            inFile.close();

            return Integer.parseInt(choice[1]);
        }
        catch (Exception ex)
        {
            return 1;
        }


    }

    public int findColour(Context con){

        String[] choice = new String[2];
        String Filereadline = "";

        try
        {

            InputStream inFile = con.openFileInput("theme.txt");
            InputStreamReader isr = new InputStreamReader(inFile);
            BufferedReader br = new BufferedReader(isr);
            Filereadline = br.readLine();
            if(Filereadline.equals(""))
            {
                choice[0] = "1";
                choice[1] = "1";

            }
            else {
                choice = Filereadline.split(",");
            }
            inFile.close();

            return Integer.parseInt(choice[0]);
        }
        catch (Exception ex)
        {
            return 1;
        }


    }


    public int findTheme(Context con)  {

        String[] choice = new String[2];
        String Filereadline = "";

        try
        {

            InputStream inFile = con.openFileInput("theme.txt");
            InputStreamReader isr = new InputStreamReader(inFile);
            BufferedReader br = new BufferedReader(isr);
            Filereadline = br.readLine();
            if(Filereadline.equals(""))
            {
                choice[0] = "1";
                choice[1] = "1";
            }
            else {
                choice = Filereadline.split(",");
            }
            inFile.close();

        }
        catch (Exception ex)
        {
            return 1;
        }


        switch (choice[0])
        {
            case "1":
                switch(choice[1])
                {
                    case "1":
                        themeSel = R.style.AppTheme;
                        break;
                    case "2":
                        themeSel = R.style.themeDefaultType;
                        break;
                    case "3":
                        themeSel = R.style.themeDefaultCond;
                        break;
                    case "4":
                        themeSel = R.style.themeDefaultNoto;
                        break;
                    case "5":
                        themeSel = R.style.themeDefaultCursive;
                        break;
                    case "6":
                        themeSel = R.style.themeDefaultCarrois;
                        break;
                    case "7":
                        themeSel = R.style.themeDefaultDroid;
                        break;
                    case "8":
                        themeSel = R.style.themeDefaultDev;
                        break;
                    default:
                        themeSel = R.style.AppTheme;
                }
               break;
            case "2":
                switch(choice[1])
                {
                    case "1":
                        themeSel = R.style.themeDark;
                        break;
                    case "2":
                        themeSel = R.style.themeDarkType;
                        break;
                    case "3":
                        themeSel = R.style.themeDarkCond;
                        break;
                    case "4":
                        themeSel = R.style.themeDarkNoto;
                        break;
                    case "5":
                        themeSel = R.style.themeDarkCursive;
                        break;
                    case "6":
                        themeSel = R.style.themeDarkCarrois;
                        break;
                    case "7":
                        themeSel = R.style.themeDarkDroid;
                        break;
                    case "8":
                        themeSel = R.style.themeDarkDev;
                        break;
                    default:
                        themeSel = R.style.AppTheme;
                }
                break;
            case"3":
                switch(choice[1])
                {
                    case "1":
                        themeSel = R.style.themeLight;
                        break;
                    case "2":
                        themeSel = R.style.themeLightType;
                        break;
                    case "3":
                        themeSel = R.style.themeLightCond;
                        break;
                    case "4":
                        themeSel = R.style.themeLightNoto;
                        break;
                    case "5":
                        themeSel = R.style.themeLightCursive;
                        break;
                    case "6":
                        themeSel = R.style.themeLightCarrois;
                        break;
                    case "7":
                        themeSel = R.style.themeLightDroid;
                        break;
                    case "8":
                        themeSel = R.style.themeLightDev;
                        break;
                    default:
                        themeSel = R.style.AppTheme;
                }
                break;
            case"4":
                switch(choice[1])
                {
                    case "1":
                        themeSel = R.style.themeCrazy;
                        break;
                    case "2":
                        themeSel = R.style.themeCrazyType;
                        break;
                    case "3":
                        themeSel = R.style.themeCrazyCond;
                        break;
                    case "4":
                        themeSel = R.style.themeCrazyNoto;
                        break;
                    case "5":
                        themeSel = R.style.themeCrazyCursive;
                        break;
                    case "6":
                        themeSel = R.style.themeCrazyCarrois;
                        break;
                    case "7":
                        themeSel = R.style.themeCrazyDroid;
                        break;
                    case "8":
                        themeSel = R.style.themeCrazyDev;
                        break;
                    default:
                        themeSel = R.style.AppTheme;
                }
                break;
            case"5":
                switch(choice[1])
                {
                    case "1":
                        themeSel = R.style.themeBrandon;
                        break;
                    case "2":
                        themeSel = R.style.themeBrandonType;
                        break;
                    case "3":
                        themeSel = R.style.themeBrandonCond;
                        break;
                    case "4":
                        themeSel = R.style.themeBrandonNoto;
                        break;
                    case "5":
                        themeSel = R.style.themeBrandonCursive;
                        break;
                    case "6":
                        themeSel = R.style.themeBrandonCarrois;
                        break;
                    case "7":
                        themeSel = R.style.themeBrandonDroid;
                        break;
                    case "8":
                        themeSel = R.style.themeBrandonDev;
                        break;
                    default:
                        themeSel = R.style.AppTheme;
                }
                break;
            case"6":
                switch(choice[1])
                {
                    case "1":
                        themeSel = R.style.themeRoyalty;
                        break;
                    case "2":
                        themeSel = R.style.themeRoyaltyType;
                        break;
                    case "3":
                        themeSel = R.style.themeRoyaltyCond;
                        break;
                    case "4":
                        themeSel = R.style.themeRoyaltyNoto;
                        break;
                    case "5":
                        themeSel = R.style.themeRoyaltyCursive;
                        break;
                    case "6":
                        themeSel = R.style.themeRoyaltyCarrois;
                        break;
                    case "7":
                        themeSel = R.style.themeRoyaltyDroid;
                        break;
                    case "8":
                        themeSel = R.style.themeRoyaltyDev;
                        break;
                    default:
                        themeSel = R.style.AppTheme;
                }
                break;
            default:
                themeSel = R.style.AppTheme;
        }
        return themeSel;
    }


}
