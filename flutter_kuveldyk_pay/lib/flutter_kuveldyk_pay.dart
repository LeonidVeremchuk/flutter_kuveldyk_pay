import 'dart:async';

import 'package:flutter/services.dart';

class FlutterKuveldykPay {
  static const MethodChannel _channel =
      const MethodChannel('flutter_kuveldyk_pay');

  static Future<Null> presentDropInDialog() async {
    await _channel.invokeMethod('');
  }
}
