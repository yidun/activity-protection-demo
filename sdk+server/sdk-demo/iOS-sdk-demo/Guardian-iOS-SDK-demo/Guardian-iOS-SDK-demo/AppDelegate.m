//
//  AppDelegate.m
//  Guardian-iOS-SDK-demo
//
//  Created by NetEase on 2017/11/9.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import "AppDelegate.h"
#import <Guardian/NTESCSGuardian.h>

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
        
    //必要步骤 初始化
    [NTESCSGuardian initWithProductNumber:@"your_product_number" completeHandler:^(NSInteger code, NSString *message) {
        
    
    }];
    
    //可选配置 设置传感器开关 默认开启 可在任何时刻调用
//    [NTESCSGuardian setSeniorCollectStatus:YES];
    //可选配置 设置渠道名称 只能在初始化时候调用
//    [NTESCSGuardian sharedInstance].channelName = @"channelName";
    //可选配置 设置私有化域名 只能在初始化时候调用
//    [NTESCSGuardian sharedInstance].urlPrefix = @"私有化域名";
    //可选配置 设置自定义数据 可在任意时刻调用
//    [NTESCSGuardian setExtraData:@"data" forKey:@"key"];
    
    return YES;
}


- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}


- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}


@end
