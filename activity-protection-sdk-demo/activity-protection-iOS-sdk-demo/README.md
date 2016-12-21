活动反作弊 iOS sdk 示例 demo
===

### demo 运行步骤

* 1、运行模拟业务后端：`check demo`，运行方法见`activity-protection-check-demo`目录
* 2、修改 `FirstViewController.m` 和 `SecondViewController.m` 的 `viewDidAppear:`，填入您的 bussinessId，如下：

		- (void)viewDidAppear:(BOOL)animated {
    		[super viewDidAppear:animated];
    		[NTESCSGuardian setBussinessId:@"YOUR_BUSINESS_ID"];
		}
* 3、如果是在真机中运行，需要修改：`DataChecker.m` 的 `sendData:`，将 `localhost` 替换为您运行`check demo`所在的终端的 `IP` 地址，如下：

		NSURL *url = [NSURL URLWithString:@"http://localhost:8181/rise.do"];
		// 例如，替换如下：
		NSURL *url = [NSURL URLWithString:@"http://10.240.132.43:8181/rise.do"];
		
至此，配置和修改完成，编译运行即可。

