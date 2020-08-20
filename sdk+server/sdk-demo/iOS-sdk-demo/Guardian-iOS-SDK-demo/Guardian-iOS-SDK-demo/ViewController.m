//
//  AsyncViewController.m
//  Guardian-iOS-SDK-demo
//
//  Created by NetEase on 2017/11/9.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import "ViewController.h"
#import <Guardian/NTESCSGuardian.h>

@interface ViewController ()

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

- (IBAction)praiseAction:(id)sender
{
    
    
    //必要步骤 获取token
    [NTESCSGuardian getTokenWithCompleteHandler:^(NSString *token, NSInteger code, NSString *message) {
       
        NSLog(@"token:%@", token);
    }];
    
    
    
    //获取版本号
    NSLog(@"version：%@", [NTESCSGuardian getSDKVersion]);
    
}



@end
