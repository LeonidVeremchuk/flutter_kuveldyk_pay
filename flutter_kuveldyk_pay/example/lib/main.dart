import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_kuveldyk_pay/flutter_kuveldyk_pay.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Container(
        color: Colors.red,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            MaterialButton(onPressed: presentDropInDialog, child: Text('Open drop in'))
          ],
        )
      )
    );
  }

  void presentDropInDialog() async {
    FlutterKuveldykPay.presentDropInDialog();
  }
}
