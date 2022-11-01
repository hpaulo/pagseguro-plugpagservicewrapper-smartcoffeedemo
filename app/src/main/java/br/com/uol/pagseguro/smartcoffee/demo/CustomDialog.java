package br.com.uol.pagseguro.smartcoffee.demo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import br.com.uol.pagseguro.smartcoffee.R;

public class CustomDialog extends Dialog {

//    @BindView(R.id.textview_message)
    TextView mTextViewMessage;

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        mTextViewMessage = findViewById(R.id.textview_message);
        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mCancelBtnClicked();
            }
        });
  //      ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(String message) {
        mTextViewMessage.setText(message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    //@OnClick(R.id.btn_cancel)
    public void mCancelBtnClicked() {
        cancel();
    }
}
