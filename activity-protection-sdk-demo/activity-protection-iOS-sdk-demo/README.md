活动反作弊 iOS sdk 示例 demo
===

### demo 运行步骤

* 1、运行模拟业务后端：`check demo`，运行方法见`activity-protection-check-demo`目录
* 2、修改 `AppDelegate.m`，填入您的productNumber。如下：
	
		-(BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
   	 		// Override point for customization after application launch.
    
    		// 非高级模式启动①
    		[NTESCSGuardian startWithProductNumber:@"your_product_number"];
    
    		// 非高级模式启动②
			// [NTESCSGuardian startWithProductNumber:@"your_product_number" advanceMode:NO];
    
 			// 高级模式启动
			// [NTESCSGuardian startWithProductNumber:@"your_product_number" advanceMode:YES];
    
    		return YES;
		}

* 3、修改 `SyncViewController.m` 和`AsyncViewController.m`，填入您的 bussinessId，如下：

		NSString *token = [NTESCSGuardian getTokenWithBusinessID:@"your_businessID" timeout:3];
    	[DataChecker checkToken:token];
  或者：
 
 		[NTESCSGuardian getTokenWithBusinessID:@"your_businessID" timeout:3 completeHandler:^(NSString *token) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [DataChecker checkToken:token];
            });
        }];
    
* 3、如果是在真机中运行，需要修改：`DataChecker.m` 的 `checkToken:`，将 `localhost` 替换为您运行`check demo`所在的终端的 `IP` 地址，如下：

		NSURL *url = [NSURL URLWithString:@"http://localhost:8181/rise.do"];
		// 例如，替换如下：
		NSURL *url = [NSURL URLWithString:@"http://10.240.132.43:8181/rise.do"];
		
至此，配置和修改完成，编译运行即可。

