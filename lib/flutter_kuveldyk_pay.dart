import 'dart:async';

import 'package:flutter/services.dart';

class FlutterKuveldykPay {

  static const MethodChannel _channel =
  const MethodChannel('flutter_kuveldyk_pay');

  static Future<dynamic> presentDropInDialog(String clientToken, String amount) async {
    assert(clientToken != null);
    assert(amount != null);

    final Map<String, Object> arguments = <String, dynamic>{
      'token': clientToken,
      'amount': amount,
    };
    var data = await _channel.invokeMethod('', arguments);
    return BraintreeResult(data['amount'], data['paymentMethod']);
  }
}


class BraintreeResult {
  String _amount;

  String get amount => this._amount;

  set amount(String value) {
    _amount = value;
  }

  String _paymentMethod;

  BraintreeResult(this._amount, this._paymentMethod);

  Map<String, dynamic> toJson() =>
      {
        "amount": _amount,
        "payment_method_nonce": _paymentMethod
      };

  String get paymentMethod => this._paymentMethod;

  set paymentMethod(String value) {
    _paymentMethod = value;
  }
}


