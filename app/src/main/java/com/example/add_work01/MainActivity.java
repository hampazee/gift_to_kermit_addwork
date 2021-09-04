package com.example.add_work01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    EditText workname;
    Button beginbtn,endbtn,securebtn;
    TextView selectMonth,selectDayBig,selectDaySmall,selectDayFeb,selectWorkType;
    EditText ed1,ed3,ed4;
    TextView ed2;
    String[] monthArray;
    String[] monthDaySmall,monthDayBig,monthDayFeb ;
    String[] workType;
    String pt_money,pt_time,day_money,month_money;


    boolean[] selectWorkDay;
    int beginTimeHour,beginTimeMinute,endTimeHour,endTimeMinute;
    double pt_worktime;

    ArrayList<Integer> DayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.linear);
        workname = findViewById(R.id.add_work_name);
        beginbtn = findViewById(R.id.add_work_begintime);
        endbtn = findViewById(R.id.add_work_endtime);
        securebtn = findViewById(R.id.add_work_secure);

        selectMonth = findViewById(R.id.singleSelect);
        selectDayBig = findViewById(R.id.t_big);
        selectDaySmall = findViewById(R.id.t_small);
        selectDayFeb = findViewById(R.id.t_feb);
        selectWorkType = findViewById(R.id.work_chooseType);

        ed1 = findViewById(R.id.work_salary_PT);
        ed2 = findViewById(R.id.work_salary_PThour);
        ed3 = findViewById(R.id.work_salary_Day);
        ed4 = findViewById(R.id.work_salary_Month);

        //---藏鍵盤事件---
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        workname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                showKeyBoard(workname);
                //get value from edittext
                String s = workname.getText().toString().trim();
                //check condition
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    //when action is equal to action done
                    //hide keyboard
                    hideKeyBoard(workname);
                    //display toast

                    return true;
                }

                return false;
            }
        });

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("TouchEvents","Touch is detected");

                int eventType = event.getActionMasked();

                switch (eventType){
                    case MotionEvent.ACTION_DOWN:
                        hideKeyBoard(workname);
                        break;
                }

                return true;
            }
        });
        //---藏鍵盤事件end---

       selectMonth.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               monthArray = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
               MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this)
                       .setTitle("選取月份").setIcon(R.drawable.ic_baseline_calendar_today_24).setCancelable(false)
                       .setBackground(getResources().getDrawable(R.drawable.rounded))
                       .setSingleChoiceItems(monthArray, -1, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                                selectMonth.setText(monthArray[which]);
                                selectDayBig.setClickable(true);
                           }
                       }).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               if(selectMonth.getText()=="1月"||selectMonth.getText()=="3月"||selectMonth.getText()=="5月"
                                       ||selectMonth.getText()=="7月"||selectMonth.getText()=="8月"||
                                       selectMonth.getText()=="10月"||selectMonth.getText()=="12月"){
                                   selectDayBig.setClickable(true);
                                   selectDayBig.setVisibility(View.VISIBLE);
                                   selectDaySmall.setVisibility(View.GONE);
                                   selectDayFeb.setVisibility(View.GONE);
                               }
                               else if(selectMonth.getText()=="4月"||selectMonth.getText()=="6月"||selectMonth.getText()=="9月"
                                       ||selectMonth.getText()=="11月"){
                                   selectDaySmall.setClickable(true);
                                   selectDaySmall.setVisibility(View.VISIBLE);
                                   selectDayBig.setVisibility(View.GONE);
                                   selectDayFeb.setVisibility(View.GONE);
                               }
                               else{
                                   selectDayFeb.setClickable(true);
                                   selectDayFeb.setVisibility(View.VISIBLE);
                                   selectDayBig.setVisibility(View.GONE);
                                   selectDaySmall.setVisibility(View.GONE);
                               }
                           }
                       }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                           }
                       });
               builder.show();
           }
       });

       selectDayBig.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               monthDayBig = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                       "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
               selectWorkDay = new boolean[monthDayBig.length];
               MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this)
                       .setTitle("選取工作日(可多選)").setIcon(R.drawable.ic_baseline_calendar_today_24)
                       .setBackground(getResources().getDrawable(R.drawable.rounded)).setCancelable(false)
                       .setMultiChoiceItems(monthDayBig, selectWorkDay, new DialogInterface.OnMultiChoiceClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                               if (isChecked) {
                                   DayList.add(which);

                                   Collections.sort(DayList);
                               } else {
                                   DayList.remove(which);
                               }
                           }
                       }).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               StringBuilder stringBuilder = new StringBuilder();
                               //use for loop
                               for (int i = 0; i < DayList.size(); i++) {
                                   //concat the value
                                   stringBuilder.append(monthDayBig[DayList.get(i)]);
                                   //check condition
                                   if (i != DayList.size() - 1) {
                                       //when i value not equal to month list size -1
                                       //add commma
                                       stringBuilder.append(",");
                                   }
                               }
                               //set text on textview
                               selectDayBig.setText(stringBuilder.toString());

                           }
                       }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       }).setNegativeButton("清除全部", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               for (int i = 0; i < selectWorkDay.length; i++) {
                               //remove all selection
                               selectWorkDay[i] = false;
                               //clear day list
                               DayList.clear();
                               //clear text view value
                               selectDayBig.setText(" ");
                           }
                           }
                       });
               builder.show();
           }
       });

       selectDaySmall.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               monthDaySmall = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                       "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
               selectWorkDay = new boolean[monthDaySmall.length];
               MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this)
                       .setTitle("選取工作日(可多選)").setIcon(R.drawable.ic_baseline_calendar_today_24)
                       .setBackground(getResources().getDrawable(R.drawable.rounded)).setCancelable(false)
                       .setMultiChoiceItems(monthDaySmall, selectWorkDay, new DialogInterface.OnMultiChoiceClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                               if (isChecked) {
                                   DayList.add(which);

                                   Collections.sort(DayList);
                               } else {
                                   DayList.remove(which);
                               }
                           }
                       }).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               StringBuilder stringBuilder = new StringBuilder();
                               //use for loop
                               for (int i = 0; i < DayList.size(); i++) {
                                   //concat the value
                                   stringBuilder.append(monthDaySmall[DayList.get(i)]);
                                   //check condition
                                   if (i != DayList.size() - 1) {
                                       //when i value not equal to month list size -1
                                       //add commma
                                       stringBuilder.append(",");
                                   }
                               }
                               //set text on textview
                               selectDaySmall.setText(stringBuilder.toString());
                           }
                       }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       }).setNegativeButton("清除全部", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               for (int i = 0; i < selectWorkDay.length; i++) {
                                   //remove all selection
                                   selectWorkDay[i] = false;
                                   //clear day list
                                   DayList.clear();
                                   //clear text view value
                                   selectDaySmall.setText(" ");
                               }
                           }
                       });
               builder.show();
           }
       });

       selectDayFeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthDayFeb = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};
                selectWorkDay = new boolean[monthDayFeb.length];
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("選取工作日(可多選)").setIcon(R.drawable.ic_baseline_calendar_today_24)
                        .setBackground(getResources().getDrawable(R.drawable.rounded)).setCancelable(false)
                        .setMultiChoiceItems(monthDayFeb, selectWorkDay, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    DayList.add(which);

                                    Collections.sort(DayList);
                                } else {
                                    DayList.remove(which);
                                }
                            }
                        }).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StringBuilder stringBuilder = new StringBuilder();
                                //use for loop
                                for (int i = 0; i < DayList.size(); i++) {
                                    //concat the value
                                    stringBuilder.append(monthDayFeb[DayList.get(i)]);
                                    //check condition
                                    if (i != DayList.size() - 1) {
                                        //when i value not equal to month list size -1
                                        //add commma
                                        stringBuilder.append(",");
                                    }
                                }
                                //set text on textview
                                selectDayFeb.setText(stringBuilder.toString());
                            }
                        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("清除全部", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < selectWorkDay.length; i++) {
                                    //remove all selection
                                    selectWorkDay[i] = false;
                                    //clear day list
                                    DayList.clear();
                                    //clear text view value
                                    selectDayFeb.setText(" ");
                                }
                            }
                        });
                builder.show();
            }
        });

        pt_worktime =(double) (endTimeHour-beginTimeHour);
        pt_money = Double.toString(pt_worktime);


        selectWorkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workType = new String[]{"時薪","日薪","月薪"};
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("選取工作型態").setCancelable(false).setIcon(R.drawable.ic_baseline_calendar_today_24)
                        .setBackground(getResources().getDrawable(R.drawable.rounded))
                        .setSingleChoiceItems(workType, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectWorkType.setText(workType[which]);


                            }
                        }).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selectWorkType.getText()=="時薪") {
                                    ed1.setVisibility(View.VISIBLE);
                                    ed2.setVisibility(View.VISIBLE);
                                    ed2.setText(String.valueOf(pt_worktime));
                                    ed3.setVisibility(View.GONE);
                                    ed4.setVisibility(View.GONE);
                                    ed3.setText("");
                                    ed4.setText("");
                                }
                                else if(selectWorkType.getText()=="日薪"){
                                    ed1.setVisibility(View.GONE);
                                    ed2.setVisibility(View.GONE);
                                    ed3.setVisibility(View.VISIBLE);
                                    ed4.setVisibility(View.GONE);
                                    ed1.setText("");
                                    ed2.setText("");
                                    ed4.setText("");
                                }
                                else{
                                    ed1.setVisibility(View.GONE);
                                    ed2.setVisibility(View.GONE);
                                    ed3.setVisibility(View.GONE);
                                    ed4.setVisibility(View.VISIBLE);
                                    ed1.setText("");
                                    ed2.setText("");
                                    ed3.setText("");
                                }
                            }
                        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });



    //ed1 ,ed2 , ed3, ed4 need to build gettext to store

        securebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workname.getText().toString()==null){
                    Toast.makeText(MainActivity.this,"名稱不可為空",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public void selectBeginTime(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectHour, int selectMinute) {
                beginTimeHour = selectHour;
                beginTimeMinute = selectMinute;
                beginbtn.setText(String.format(Locale.getDefault(),"%02d : %02d",beginTimeHour,beginTimeMinute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,onTimeSetListener,beginTimeHour,beginTimeMinute,true);

        timePickerDialog.setTitle("選取時間");
        timePickerDialog.show();
    }

    public void selectEndTime(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectHour, int selectMinute) {
                endTimeHour = selectHour;
                endTimeMinute = selectMinute;
                endbtn.setText(String.format(Locale.getDefault(),"%02d : %02d",endTimeHour,endTimeMinute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,onTimeSetListener,endTimeHour,endTimeMinute,true);

        timePickerDialog.setTitle("選取時間");
        timePickerDialog.show();
    }

    private void hideKeyBoard(EditText editText) {
        //initialize input manager
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //hide soft keyboard
        manager.hideSoftInputFromWindow(editText.getApplicationWindowToken(),0);
    }

    private void showKeyBoard(EditText editText) {
        //initialize input manager
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //show soft keyboard
        manager.showSoftInput(editText.getRootView(),InputMethodManager.SHOW_IMPLICIT);
        //focus on edittext
        editText.requestFocus();
    }
}