package com.snailapp.flutter_kuveldyk_pay;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;

/** FlutterKuveldykPayPlugin */
public class FlutterKuveldykPayPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener {
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_kuveldyk_pay");
    final FlutterKuveldykPayPlugin plugin = new FlutterKuveldykPayPlugin(registrar.activity());
    channel.setMethodCallHandler(plugin);
    registrar.addActivityResultListener(plugin);
  }

  private Activity mActivity;

  final int DROP_IN_REQUEST_CODE = 3074;
// public static final String TOKEN = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpGVXpJMU5pSXNJbXRwWkNJNklqSXdNVGd3TkRJMk1UWXRjMkZ1WkdKdmVDSjkuZXlKbGVIQWlPakUxTmpBd01UQTBOVFVzSW1wMGFTSTZJakUwTnprME1USTRMVEZsWWpFdE5HTTBNQzA0TWpKbUxUazBaVFkwTldWbVpqY3pPQ0lzSW5OMVlpSTZJbk0wWkRsalkyWnFPV3Q0T1hFMk1tSWlMQ0pwYzNNaU9pSkJkWFJvZVNJc0ltMWxjbU5vWVc1MElqcDdJbkIxWW14cFkxOXBaQ0k2SW5NMFpEbGpZMlpxT1d0NE9YRTJNbUlpTENKMlpYSnBabmxmWTJGeVpGOWllVjlrWldaaGRXeDBJanBtWVd4elpYMHNJbkpwWjJoMGN5STZXeUp0WVc1aFoyVmZkbUYxYkhRaVhTd2liM0IwYVc5dWN5STZlMzE5LkFCWnVvMnRsZTM2WEQ4YWM0Y2VielFtSk9PSXVhN182amhIQV80VDFVVnNjek5Fa1AzSU5GMWNacVNWVGxoVE1KZ2RzYnExVTNYQ1c1R0RYMVYwNmZnIiwiY29uZmlnVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL3M0ZDljY2ZqOWt4OXE2MmIvY2xpZW50X2FwaS92MS9jb25maWd1cmF0aW9uIiwiZ3JhcGhRTCI6eyJ1cmwiOiJodHRwczovL3BheW1lbnRzLnNhbmRib3guYnJhaW50cmVlLWFwaS5jb20vZ3JhcGhxbCIsImRhdGUiOiIyMDE4LTA1LTA4In0sImNoYWxsZW5nZXMiOltdLCJlbnZpcm9ubWVudCI6InNhbmRib3giLCJjbGllbnRBcGlVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvczRkOWNjZmo5a3g5cTYyYi9jbGllbnRfYXBpIiwiYXNzZXRzVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhdXRoVXJsIjoiaHR0cHM6Ly9hdXRoLnZlbm1vLnNhbmRib3guYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhbmFseXRpY3MiOnsidXJsIjoiaHR0cHM6Ly9vcmlnaW4tYW5hbHl0aWNzLXNhbmQuc2FuZGJveC5icmFpbnRyZWUtYXBpLmNvbS9zNGQ5Y2NmajlreDlxNjJiIn0sInRocmVlRFNlY3VyZUVuYWJsZWQiOnRydWUsInBheXBhbEVuYWJsZWQiOnRydWUsInBheXBhbCI6eyJkaXNwbGF5TmFtZSI6Im5vbmUsYSIsImNsaWVudElkIjoiQVFMdzZuM3lBQVFEb1JlTzZOR2hwbnUza2VnZ0FKcUhlNEZKeld4WFVMUHVZMDMxNjhZRkEtOGJQbVAyZDM3YnNKWVVnVEdOMnNMczcyMzEiLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjpmYWxzZSwiZW52aXJvbm1lbnQiOiJvZmZsaW5lIiwidW52ZXR0ZWRNZXJjaGFudCI6ZmFsc2UsImJyYWludHJlZUNsaWVudElkIjoibWFzdGVyY2xpZW50MyIsImJpbGxpbmdBZ3JlZW1lbnRzRW5hYmxlZCI6dHJ1ZSwibWVyY2hhbnRBY2NvdW50SWQiOiJub25lYSIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJtZXJjaGFudElkIjoiczRkOWNjZmo5a3g5cTYyYiIsInZlbm1vIjoib2ZmIn0=";
  public static final String TOKEN = "sandbox_pgt33dgj_s4d9ccfj9kx9q62b";
  private PaymentCall lastPaymentCall;
  private FlutterKuveldykPayPlugin(Activity activity) {
    this.mActivity = activity;
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("")) {
      lastPaymentCall = new PaymentCall(call, result);
      mActivity.runOnUiThread(() -> {
        DropInRequest request = new DropInRequest().clientToken(lastPaymentCall.token()).amount(lastPaymentCall.amount());
        mActivity.startActivityForResult(request.getIntent(mActivity), DROP_IN_REQUEST_CODE);

      });
    } else {
      result.notImplemented();
    }
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
    Log.i("PAYMENT", "requestCode: " + requestCode);
    Log.i("PAYMENT", "resultCode: " + resultCode);
    if (requestCode == DROP_IN_REQUEST_CODE) {
      switch (resultCode) {
        case Activity.RESULT_OK:
          final DropInResult result = intent.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
          final String nonce = result.getPaymentMethodNonce().getNonce();
          lastPaymentCall.success(nonce);
          break;
        case Activity.RESULT_CANCELED:
          break;
          default:
            //TODO handle error
            break;
      }
    }
    return false;
  }
}
