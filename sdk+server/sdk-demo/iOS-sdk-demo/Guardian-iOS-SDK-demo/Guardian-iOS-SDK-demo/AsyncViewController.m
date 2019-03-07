//
//  AsyncViewController.m
//  Guardian-iOS-SDK-demo
//
//  Created by NetEase on 2017/11/9.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import "AsyncViewController.h"
#import "DataChecker.h"
#import <Guardian/NTESCSGuardian.h>

@interface AsyncViewController ()

@end

@implementation AsyncViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)praiseAction:(id)sender
{
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        [NTESCSGuardian getTokenWithBusinessID:@"your_businessID" timeout:3 unit:NTESCSTimeOutSecondUnit completeHandler:^(NSString *token) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [DataChecker checkToken:token];
            });
        }];
    });
}

- (IBAction)startAction:(id)sender
{
    // 如果SDK以高级模式启动，请在需要开始工作的时候调用SDK start 方法
//    [NTESCSGuardian start];
}

- (IBAction)stopAction:(id)sender
{
    // 如果SDK以高级模式启动，请在需要停止工作的时候调用SDK stop 方法
//    [NTESCSGuardian stop];
}

@end
