package br.com.uol.pagseguro.smartcoffee.printer;

import android.os.Environment;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrintResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrinterData;
import br.com.uol.pagseguro.smartcoffee.ActionResult;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class PrinterUseCase {

    private final PlugPag mPlugPag;

    public PrinterUseCase(PlugPag plugPag) {
        mPlugPag = plugPag;
    }

    public Observable<ActionResult> printFile() {
        return Observable.create((ObservableEmitter<ActionResult> emitter) -> {

            ActionResult actionResult = new ActionResult();
            setPrintListener(emitter, actionResult);

            PlugPagPrintResult result = mPlugPag.printFromFile(new PlugPagPrinterData(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/goku.jpg",
                    4,
                    10 * 12));

            if (result.getResult() != 0)
                actionResult.setResult(result.getResult());

            emitter.onComplete();
        });
    }

    private void setPrintListener(ObservableEmitter<ActionResult> emitter, ActionResult result) {
        mPlugPag.setPrinterListener(printResult -> {
            result.setMessage(printResult.getMessage());
            result.setErrorCode(printResult.getErrorCode());
            result.setResult(printResult.getResult());
            emitter.onNext(result);
        });
    }
}
