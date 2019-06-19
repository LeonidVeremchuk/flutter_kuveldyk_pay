import Flutter
import UIKit
import BraintreeDropIn
import Braintree

public class SwiftFlutterKuveldykPayPlugin: NSObject, FlutterPlugin {
    var flutterResult: FlutterResult!;
    private var backView: UIView?
    
    func paymnetComplete(_ payment: NSObject) {
        flutterResult(payment)
    }
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_kuveldyk_pay", binaryMessenger: registrar.messenger())
        registrar.addMethodCallDelegate(SwiftFlutterKuveldykPayPlugin(), channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        flutterResult = result;
        let paymentResult = NSMutableDictionary()
        let arguments = call.arguments as! NSDictionary
        guard let currentViewController = UIApplication.shared.keyWindow?.topMostViewController() else {
            return
        }
        
        guard let amount = arguments["amount"] as? String else {return}
        guard let token = arguments["token"] as? String else {return}
        
        let request =  BTDropInRequest()
        request.vaultManager = true
        request.amount = amount
        let dropIn = BTDropInController(authorization: token, request: request)
        {
            (controller, res, error) in
            if (error != nil) {
                self.flutterResult("ERROR")
            } else if (res?.isCancelled == true) {
                self.flutterResult("CANCELLED")
            } else if let res = res {
                paymentResult["paymentMethod"] = res.paymentMethod?.nonce
                paymentResult["paymentOptionType"] = res.paymentOptionType.rawValue as Int
                paymentResult["paymentDescription"] = res.paymentDescription.description as String
                paymentResult["amount"] = "1.0"
                self.paymnetComplete(paymentResult)
            }
            
            currentViewController.dismiss(animated: true, completion: nil)
        }
        
        let tapDropIn = UITapGestureRecognizer(target: self, action: #selector(tapdropInAction))
        
        dropIn?.view.addGestureRecognizer(tapDropIn)
        
        currentViewController.present(dropIn!, animated: true, completion: nil)
    }
    
    @objc func tapdropInAction(recognizer: UITapGestureRecognizer) {
        guard let currentViewController = UIApplication.shared.keyWindow?.topMostViewController() else {
            return
        }
        self.flutterResult("CANCELLED")
        currentViewController.dismiss(animated: true, completion: nil)
    }
}

extension UIWindow {
    func topMostViewController() -> UIViewController? {
        
        guard let rootViewController = self.rootViewController else {
            return nil
        }
        
        let viewController = topViewController(for: rootViewController)
        
        return viewController
    }
    
    func topViewController(for rootViewController: UIViewController?) -> UIViewController? {
        guard let rootViewController = rootViewController else {
            return nil
        }
        
        guard let presentedViewController = rootViewController.presentedViewController else {
            return rootViewController
        }
        
        switch presentedViewController {
        case is UINavigationController:
            let navigationController = presentedViewController as! UINavigationController
            return topViewController(for: navigationController.viewControllers.last)
        case is UITabBarController:
            let tabBarController = presentedViewController as! UITabBarController
            return topViewController(for: tabBarController.selectedViewController)
        default:
            return topViewController(for: presentedViewController)
        }
    }
}
