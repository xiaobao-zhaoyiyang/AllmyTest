package yeeaoo.sqlitedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by yo on 2016/4/13.
 */
public class CalderActivity extends AppCompatActivity implements View.OnClickListener {
    private Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, bC, bjian, bDEL, bcheng, bchu, bdeng,
                    dian, bjia;
    private EditText ET;
    private boolean clear_flag;//清空标示

    private StringBuffer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calderlayout);
        b0 = (Button) findViewById(R.id.button2);
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        b5 = (Button) findViewById(R.id.b5);
        b6 = (Button) findViewById(R.id.b6);
        b7 = (Button) findViewById(R.id.b7);
        b8 = (Button) findViewById(R.id.b8);
        b9 = (Button) findViewById(R.id.b9);
        bC = (Button) findViewById(R.id.c);
        bDEL = (Button) findViewById(R.id.cel);
        dian = (Button) findViewById(R.id.dian);
        bjia = (Button) findViewById(R.id.jia);
        bjian = (Button) findViewById(R.id.jian);
        bcheng = (Button) findViewById(R.id.cheng);
        bchu = (Button) findViewById(R.id.chu);
        bdeng = (Button) findViewById(R.id.deng);
        ET = (EditText) findViewById(R.id.ett);

        setClick();
    }

    private void setClick(){
        b0.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        bC.setOnClickListener(this);
        bjia.setOnClickListener(this);
        bjian.setOnClickListener(this);
        bcheng.setOnClickListener(this);
        bchu.setOnClickListener(this);
        bdeng.setOnClickListener(this);
        dian.setOnClickListener(this);
        bDEL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String str = ET.getText().toString();
        switch (v.getId()) {
            case R.id.button2:
            case R.id.b1:
            case R.id.b2:
            case R.id.b3:
            case R.id.b4:
            case R.id.b5:
            case R.id.b6:
            case R.id.b7:
            case R.id.b8:
            case R.id.b9:
                if (clear_flag) {
                    clear_flag = false;
                    ET.setText("");
                    str = "";
                }
                ET.setText(str + ((Button) v).getText());
                break;
            case R.id.dian:
                if (clear_flag) {
                    clear_flag = false;
                    ET.setText("");
                    str = "";
                }
                if (str != null && str.equals("")){ // 如果当前没有内容，则点不起作用
                    ET.setText("");
                }else {
                    ET.setText(str + ((Button) v).getText());
                }
                break;
            case R.id.jia:
            case R.id.jian:
            case R.id.cheng:
            case R.id.chu:
                if (clear_flag) {
                    clear_flag = false;
                    ET.setText("");
                }
                ET.setText(str + " " + ((Button) v).getText() + " ");
                break;
            case R.id.c:
                clear_flag = false;
                ET.setText("");
                break;
            case R.id.cel:
                if (clear_flag) {
                    clear_flag = false;
                    ET.setText("");
                } else if (str != null && !str.equals("")) {
                    ET.setText(str.substring(0, str.length() - 1));
                }
                break;

            case R.id.deng:
                getResult();
                break;
        }
    }

    private void getResult() {
        String exp = ET.getText().toString();
        if (exp == null || exp.equals(" ")) {
            return;
        }
        if (!exp.contains("")) {
            return;
        }
        if (clear_flag) {
            clear_flag = false;
            return;
        }
        clear_flag = true;
        double result = 0;
        if (!exp.contains(" ")){
            ET.setText(exp);
            return;
        }
        String s1 = exp.substring(0, exp.indexOf(" "));
        String op = exp.substring(exp.indexOf(" ") + 1, exp.indexOf(" ") + 2);
        String s2 = exp.substring(exp.indexOf(" ") + 3);
        if (!s1.equals("") && !s2.equals("")) {
            double d1 = Double.parseDouble(s1);
            double d2 = Double.parseDouble(s2);
            if (op.equals("+")) {
                result = d1 + d2;
            } else if (op.equals("-")) {
                result = d1 - d2;
            } else if (op.equals("*")) {
                result = d1 * d2;
            } else if (op.equals("/")) {
                if (d2 == 0) {
                    result = 0;
                } else {
                    result = d1 / d2;
                }
            }
            if (!s1.contains(".") && !s2.contains(".")) {
                if (!op.equals("/")) {
                    int r = (int) result;
                    ET.setText(r + "");
                }else{
                    String s = String.valueOf(result);
                    if (s.endsWith(".0")) {
                        ET.setText(s.substring(0, s.length() - 2));
                    }else {
                        ET.setText(result + "");
                    }
                }
            } else {
                ET.setText(result + "");
            }
        } else if (!s1.equals("") && s2.equals("")) {
            ET.setText(s1);
        } else if (s1.equals("") && !s2.equals("")) {
            double d2 = Double.parseDouble(s2);
            if (op.equals("+")) {
                result = 0 + d2;
            } else if (op.equals("-")) {
                result = 0 - d2;
            } else if (op.equals("*")) {
                result = 0;
            } else if (op.equals("/")) {
                result = 0;
            }
            if (!s2.contains(".")) {
                int r = (int) result;
                ET.setText(r + "");
            } else {
                ET.setText(result + "");
            }

        } else {
            ET.setText("");
        }

    }
}
