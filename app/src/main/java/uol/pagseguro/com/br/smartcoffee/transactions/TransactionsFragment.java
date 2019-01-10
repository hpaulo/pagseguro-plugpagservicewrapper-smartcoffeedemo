package uol.pagseguro.com.br.smartcoffee.transactions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import uol.pagseguro.com.br.smartcoffee.MainActivity;
import uol.pagseguro.com.br.smartcoffee.R;
import uol.pagseguro.com.br.smartcoffee.injection.UseCaseModule;
import uol.pagseguro.com.br.smartcoffee.injection.DaggerTransactionsComponent;
import uol.pagseguro.com.br.smartcoffee.injection.TransactionsComponent;
import uol.pagseguro.com.br.smartcoffee.utils.UIFeedback;

public class TransactionsFragment extends MvpFragment<TransactionsContract, TransactionsPresenter> implements TransactionsContract {

    @Inject
    TransactionsComponent mInjector;

    public static TransactionsFragment getInstance() {
        return new TransactionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInjector = DaggerTransactionsComponent.builder()
                .useCaseModule(new UseCaseModule())
                .mainComponent(((MainActivity) getContext()).getMainComponent())
                .build();
        mInjector.inject(this);
        View rootView = inflater.inflate(R.layout.fragment_transactions, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public TransactionsPresenter createPresenter() {
        return mInjector.presenter();
    }

    @OnClick(R.id.btn_smartpos_credit)
    public void onCreditClicked() {
        getPresenter().creditPayment();
    }

    @OnClick(R.id.btn_smartpos_credit_with_seller_installments)
    public void onCreditWithSellerInstallmentsClicked() {
        getPresenter().doCreditPaymentWithSellerInstallments();
    }

    @OnClick(R.id.btn_smartpos_credit_with_buyer_installments)
    public void onCreditWithBuyerInstallmentsClicked() {
        getPresenter().doCreditPaymentWithBuyerInstallments();
    }

    @OnClick(R.id.btn_smartpos_debit)
    public void onDebitClicked() {
        getPresenter().doDebitPayment();
    }

    @OnClick(R.id.btn_smartpos_voucher)
    public void onVoucherClicked() {
        getPresenter().doVoucherPayment();
    }

    @OnClick(R.id.btn_smartpos_void_payment)
    public void onRefundClicked() {
        getPresenter().doRefundPayment();
    }

    @Override
    public void showPaymentSuccess() {
        UIFeedback.showDialog(getContext(), R.string.transactions_successful_payment);
    }

    @Override
    public void showMessage(String message) {
        UIFeedback.showDialog(getContext(), message, false);
    }

    @Override
    public void showError(String message) {
        UIFeedback.showDialog(getContext(), message);
    }

    @Override
    public void setCancelableDialog() {
//        UIFeedback.setCancelable(true);
    }
}