//
//  AsyncViewController.m
//  Guardian-iOS-SDK-demo
//
//  Created by NetEase on 2017/11/9.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import "ViewController.h"
#import <Guardian/NTESCSGuardian.h>
#import "MachineVerification.h"
#import "AppDelegate.h"

#define WeakSelf(type) __weak __typeof__(type) weakSelf = type;

@interface ViewController ()

@property (nonatomic,copy)NSString *token;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)getTokenAction:(id)sender
{

    //必要步骤 获取token
    WeakSelf(self);
    [NTESCSGuardian getTokenWithCompleteHandler:^(NSString *token, NSInteger code, NSString *message) {
       
        NSLog(@"token:%@", token);
        weakSelf.token = token;
    }];
    
    
    
    //获取版本号
    NSLog(@"version：%@", [NTESCSGuardian getSDKVersion]);
    
}

- (IBAction)checkAction:(id)sender {
    
    //check流程不能放在客户端进行 此处仅做演示  需要再服务端实现check接口的调用
    if (_token.length == 0) {
        
        return;
    }
    
    [MachineVerification isMachine:@"https://ac.dun.163yun.com/v3/common/check" token:self.token bussinessKey:business_key secretId:secret_ID  secretKey:secret_key];
    
    
}


@end
