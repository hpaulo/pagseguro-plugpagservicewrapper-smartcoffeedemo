package br.com.uol.pagseguro.smartcoffee.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import javax.inject.Inject;

import br.com.uol.pagseguro.smartcoffee.ActionResult;
import br.com.uol.pagseguro.smartcoffee.R;
import br.com.uol.pagseguro.smartcoffee.injection.DaggerDemoInternoComponent;
import br.com.uol.pagseguro.smartcoffee.injection.DemoInternoComponent;
import br.com.uol.pagseguro.smartcoffee.injection.UseCaseModule;
import br.com.uol.pagseguro.smartcoffee.injection.WrapperModule;
import br.com.uol.pagseguro.smartcoffee.utils.FileHelper;
import br.com.uol.pagseguro.smartcoffee.utils.UIFeedback;

public class DemoInternoActivity extends MvpActivity<DemoInternoContract, DemoInternoPresenter> implements DemoInternoContract {

    private static final double COFFEE_VALUE = 1.50;

    private int coffeeAmount = 1;

    CustomDialog dialog;

    @Inject
    DemoInternoComponent mInjector;

    //@BindView(R.id.txtCoffeeAmount)
    TextView mCoffeeAmountTextview;

    //@BindView(R.id.txtTotalValue)
    TextView mTotalValue;
    private boolean shouldShowDialog;

    private boolean mCanClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInjector = DaggerDemoInternoComponent.builder()
                .useCaseModule(new UseCaseModule())
                .wrapperModule(new WrapperModule(getApplicationContext()))
                .build();
        mInjector.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_selection);

        dialog = new CustomDialog(this);
        dialog.setOnCancelListener(cancelListener);

        //@BindView(R.id.txtCoffeeAmount)
         mCoffeeAmountTextview = findViewById(R.id.txtCoffeeAmount);

        //@BindView(R.id.txtTotalValue)
         mTotalValue = findViewById(R.id.txtTotalValue);


        Button btnPlus = findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlusClicked();
            }
        });

        Button btnMinus = findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMinusClicked();
            }
        });

        Button btnCredit = findViewById(R.id.btnCredit);
        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreditClicked();
            }
        });

        Button btnDebit = findViewById(R.id.btnDebit);
        btnDebit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onDebitClicked();
            }
        });

        Button btnVoucher = findViewById(R.id.btnVoucher);
        btnVoucher.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onVoucherClicked();
            }
        });

        Button btnLastTransaction = findViewById(R.id.btn_lasttransaction);
        btnLastTransaction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                lastTransaction();
            }
        });

        Button btnRefundLastTransaction = findViewById(R.id.btnRefund);
        btnRefundLastTransaction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onRefundClicked();
            }
        });
    }

    @NonNull
    @Override
    public DemoInternoPresenter createPresenter() {
        return mInjector.presenter();
    }

    // @OnClick(R.id.btnMinus)
    public void onMinusClicked() {
        if (coffeeAmount <= 1) {
            return;
        }

        coffeeAmount--;
        mCoffeeAmountTextview.setText(getResources().getQuantityString(R.plurals.coffe_amount, coffeeAmount, coffeeAmount));
        setValue(mTotalValue, false);
    }

    // @OnClick(R.id.btnPlus)
    public void onPlusClicked() {
        coffeeAmount++;
        mCoffeeAmountTextview.setText(getResources().getQuantityString(R.plurals.coffe_amount, coffeeAmount, coffeeAmount));
        setValue(mTotalValue, true);
    }

    private void setValue(TextView textView, Boolean shouldAdd) {
        String currentValue = textView.getText().toString();
        String formatedCurrentValue = currentValue.replace(",", ".").replace("R$ ", "");
        double newValue = Double.valueOf(formatedCurrentValue) + (shouldAdd ? COFFEE_VALUE : -COFFEE_VALUE);
        mTotalValue.setText(getString(R.string.total_amount, newValue));
    }

    private int getValue() {
        return Integer.valueOf(mTotalValue.getText().toString().replace("R$ ", "").replace(",", ""));
    }

    // @OnClick(R.id.btnCredit)
    public void onCreditClicked() {
        if (!mCanClick) {
            return;
        }
        mCanClick = false;
        shouldShowDialog = true;
        getPresenter().creditPayment(getValue());
    }

    // @OnClick(R.id.btnDebit)
    public void onDebitClicked() {
        if (!mCanClick) {
            return;
        }
        mCanClick = false;
        shouldShowDialog = true;
        getPresenter().doDebitPayment(getValue());
    }

    // @OnClick(R.id.btnVoucher)
    public void onVoucherClicked() {
        if (!mCanClick) {
            return;
        }
        mCanClick = false;
        shouldShowDialog = true;
        getPresenter().doVoucherPayment(getValue());
    }

    // @OnClick(R.id.btn_lasttransaction)
    public void lastTransaction() {
        shouldShowDialog = true;
        getPresenter().getLastTransaction();
    }

    // @OnClick(R.id.btnRefund)
    public void onRefundClicked() {
        if (!mCanClick) {
            return;
        }

        shouldShowDialog = true;
        mCanClick = false;
        getPresenter().doRefund(FileHelper.readFromFile(this));
    }

    @Override
    public void showTransactionSuccess() {
        mCanClick = true;
        showMessage(getString(R.string.transactions_successful));
    }

    @Override
    public void writeToFile(String transactionCode, String transactionId) {
        FileHelper.writeToFile(transactionCode, transactionId, this);
    }

    @Override
    public void disposeDialog() {
        mCanClick = true;
        shouldShowDialog = false;
    }

    @Override
    public void showAuthProgress(String message) {
        UIFeedback.showDialog(this, message);
    }

    @Override
    public void showMessage(String message) {
        if (shouldShowDialog && !dialog.isShowing()) {
            dialog.show();
        }
        dialog.setMessage(message);
    }

    @Override
    public void showError(String message) {
        UIFeedback.showDialog(this, message);
    }

    @Override
    public void showAbortedSuccessfully() {
        UIFeedback.showDialog(this, R.string.transactions_successful_abort, true);
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            UIFeedback.showProgress(this);
        } else {
            UIFeedback.dismissProgress();
        }
    }

    @Override
    public void showActivationDialog() {
        ActivationDialog dialog = new ActivationDialog();
        dialog.setOnDismissListener(new DismissListener() {
            @Override
            public void onDismiss(String activationCode) {
                getPresenter().activate(activationCode);
            }
        });
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    DialogInterface.OnCancelListener cancelListener = dialogInterface -> {
        dialogInterface.dismiss();
        if (shouldShowDialog) {
            getPresenter().abortTransaction();
        }
    };

    @Override
    public void onDestroy() {
        UIFeedback.releaseVariables();
        super.onDestroy();
    }
}
