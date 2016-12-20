//
//  FirstViewController.m
//  activity-protection-iOS-sdk-demo
//
//  Created by NetEase on 16/12/20.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import "FirstViewController.h"
#import <Guardian/NTESCSGuardian.h>
#import "DataChecker.h"

@interface FirstViewController ()

@end

@implementation FirstViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [NTESCSGuardian setBussinessId:@"YOUR_BUSINESS_ID"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)pressFavourite:(id)sender {
    NSString *token = [NTESCSGuardian getToken:3];
    NSAssert(token != nil, @"token is nil，fatal error");
    [DataChecker sendData:token];
}

@end
