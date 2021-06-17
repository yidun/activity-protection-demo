活动反作弊 iOS sdk 示例 demo
===

### demo 运行步骤

* 1、运行模拟业务后端：`check demo`，运行方法见`activity-protection-check-demo`目录

* 2、修改相关配置

```
#define product_number @"your_product_number"
#define business_key @"your_business_key"
#define secret_key @"your_secret_key"
#define secret_ID @"your_secret_ID"
```
* 2、修改 `AppDelegate.m`，填入您的productNumber。如下：
	
		-(BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
   	 		// Override point for customization after application launch.
    
    		// 初始化
            [NTESCSGuardian initWithProductNumber:product_number completeHandler:^(NSInteger code, NSString *message) {
                
            
            }];
    
    
    		return YES;
		}

* 3、获取token，如下：

        [NTESCSGuardian getTokenWithCompleteHandler:^(NSString *token, NSInteger code, NSString *message) {
   
            NSLog(@"token:%@", token);
        }];
    

		
至此，配置和修改完成，编译运行即可。

