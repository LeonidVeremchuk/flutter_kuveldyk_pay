#import "FlutterKuveldykPayPlugin.h"
#import <flutter_kuveldyk_pay/flutter_kuveldyk_pay-Swift.h>

@implementation FlutterKuveldykPayPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterKuveldykPayPlugin registerWithRegistrar:registrar];
}
@end
