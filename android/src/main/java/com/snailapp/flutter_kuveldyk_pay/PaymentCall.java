package com.snailapp.flutter_kuveldyk_pay;

import android.util.Log;

import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;

import static io.flutter.plugin.common.MethodChannel.*;

public class PaymentCall {

    private static final String KEY_TOKEN = "token";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_NONCE = "paymentMethod";

    private final MethodCall call;
    private final Result result;

    public PaymentCall(MethodCall call, Result result) {
        this.call = call;
        this.result = result;
    }

    public String amount() {
        return call.argument(KEY_AMOUNT);
    }

    public String token() {
        return call.argument(KEY_TOKEN);
    }

    public void success(final String nonce) {
        final HashMap<String, Object> arguments = new HashMap<>();
        arguments.put(KEY_AMOUNT, amount());
        arguments.put(KEY_NONCE, nonce);
        Log.i("PAYMENT", "args: " + arguments.toString());
        result.success(arguments);
    }

}
