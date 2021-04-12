活动反作弊 iOS sdk 示例 demo
===

### demo 运行步骤

* 1、运行模拟业务后端：`check demo`，运行方法见`activity-protection-check-demo`目录
* 2、修改 `AppDelegate.m`，填入您的productNumber。如下：
	
		-(BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
   	 		// Override point for customization after application launch.
    
    		// 初始化
            [NTESCSGuardian initWithProductNumber:@"your_product_number" completeHandler:^(NSInteger code, NSString *message) {
                
            
            }];
    
    
    		return YES;
		}

* 3、获取token，如下：

        [NTESCSGuardian getTokenWithCompleteHandler:^(NSString *token, NSInteger code, NSString *message) {
   
            NSLog(@"token:%@", token);
        }];
    

		
至此，配置和修改完成，编译运行即可。

