import 'package:flutter/material.dart';
import 'dart:async';

import 'package:http/http.dart' as http;
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

    BraintreeResult data = await FlutterKuveldykPay.presentDropInDialog('sandbox_pgt33dgj_s4d9ccfj9kx9q62b', '1.0');
    print(data.toJson());
    await http.post("http://localhost:3000/checkouts", body: data.toJson()).then((result) {
      print(result);
    });
  }
}

