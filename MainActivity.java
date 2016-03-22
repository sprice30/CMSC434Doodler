package com.example.samprice.cmsc434doodle;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.util.TypedValue;




public class MainActivity extends Activity implements OnClickListener {

    private DoodleView _doodleView;
    private ImageButton _currPaint, _drawButton, _eraseButton, _newButton, _saveButton, _opaciButton;
    private float _smallBrush, _mediumBrush, _largeBrush;
    private boolean _erase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _doodleView = (DoodleView)findViewById(R.id.drawing);
        _smallBrush = getResources().getInteger(R.integer.small_size);
        _mediumBrush = getResources().getInteger(R.integer.medium_size);
        _largeBrush = getResources().getInteger(R.integer.large_size);
        _drawButton = (ImageButton)findViewById(R.id.draw_btn);
        _drawButton.setOnClickListener(this);



        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);

        _currPaint = (ImageButton)paintLayout.getChildAt(0);

        _currPaint.setImageDrawable(getResources().getDrawable(R.drawable.color_selected));

        _doodleView.setBrushSize(_mediumBrush);

        _eraseButton = (ImageButton)findViewById(R.id.erase_btn);
        _eraseButton.setOnClickListener(this);

        _newButton = (ImageButton)findViewById(R.id.new_btn);
        _newButton.setOnClickListener(this);

        _saveButton = (ImageButton)findViewById(R.id.save_btn);
        _saveButton.setOnClickListener(this);

        _opaciButton = (ImageButton)findViewById(R.id.opacity_btn);
        _opaciButton.setOnClickListener(this);
    }

    public void paintClicked(View view) {
        _doodleView.setErase(false);
        _doodleView.setBrushSize(_doodleView.getLastBrushSize());
        if(view!=_currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            _doodleView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.color_selected));
            _currPaint.setImageDrawable(getResources().getDrawable(R.drawable.painting));
            _currPaint=(ImageButton)view;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.draw_btn){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.choose_brush);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    _doodleView.setBrushSize(_smallBrush);
                    _doodleView.setLastBrushSize(_smallBrush);
                    _doodleView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    _doodleView.setBrushSize(_mediumBrush);
                    _doodleView.setLastBrushSize(_mediumBrush);
                    _doodleView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    _doodleView.setBrushSize(_largeBrush);
                    _doodleView.setLastBrushSize(_largeBrush);
                    _doodleView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        } else if(v.getId()==R.id.erase_btn){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.choose_brush);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    _doodleView.setErase(true);
                    _doodleView.setBrushSize(_smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    _doodleView.setErase(true);
                    _doodleView.setBrushSize(_mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    _doodleView.setErase(true);
                    _doodleView.setBrushSize(_largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(v.getId()==R.id.new_btn){
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    _doodleView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(v.getId()==R.id.save_btn){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    _doodleView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), _doodleView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "drawing");
                    if (imgSaved != null) {
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }

                }


            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();

            _doodleView.destroyDrawingCache();
        }
        else if(v.getId()==R.id.opacity_btn){
            final Dialog seekDialog = new Dialog(this);
            seekDialog.setTitle("Opacity level:");
            seekDialog.setContentView(R.layout.opacity_selected);

            final TextView seekTxt = (TextView)seekDialog.findViewById(R.id.opq_txt);
            final SeekBar seekOpq = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);

            seekOpq.setMax(100);

            int currLevel = _doodleView.getPaintAlpha();
            seekTxt.setText(currLevel+"%");
            seekOpq.setProgress(currLevel);

            seekOpq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress) + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            Button opqBtn = (Button)seekDialog.findViewById(R.id.opq_ok);
            opqBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    _doodleView.setPaintAlpha(seekOpq.getProgress());
                    seekDialog.dismiss();
                }
            });
            seekDialog.show();

        }

        }
    }




