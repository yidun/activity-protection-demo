//
//  NTESCSGuardian.h
//  NTESCSGuardian
//
//  Created by NetEase on 16/10/19.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NTESCSGuardian: NSObject

/**
 设置 bussiness Id

 @param Id bussiness Id
 */
+ (void)setBussinessId:(NSString *)Id;

/**
 同步方式返回 token，默认超时时间 3s，建议在非主线程中调用
 
 @return 未加密的 token 字符串
 */
+ (NSString *)getToken;

/**
 同步方式返回 token，建议在非主线程中调用

 @param timeout 超时时间（范围：1s ~ 10s）
 @return 未加密的 token 字符串
 */
+ (NSString *)getToken:(int)timeout;

@end
