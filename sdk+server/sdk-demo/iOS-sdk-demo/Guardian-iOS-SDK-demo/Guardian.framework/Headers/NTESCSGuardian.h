//
//  NTESCSGuardian.h
//  NTESCSGuardian
//
//  Created by NetEase on 16/10/19.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^ initBlock)(NSInteger code, NSString *message);

typedef void (^ NdInfoBlock)(NSString *dToken, NSInteger code, NSString *message);

typedef void (^ tokenBlock)(NSString *token, NSInteger code, NSString *message);

@interface NTESCSGuardian: NSObject

/**
 urlPrefix 私有化域名，默认值为 https://ac.dun.163yun.com
 */

@property (copy, nonatomic) NSString *urlPrefix;
/**
 channelName 渠道名称，默认值为 "App Store"
 */
@property (copy, nonatomic) NSString *channelName;

/**
 传感器数据是否采集开关 默认为开启。 YES为开启，NO为关闭
*/
@property (assign, nonatomic) BOOL seniorCollectStatus;

/**
获取单例方法 设置属性时使用
*/
+ (instancetype)sharedInstance;

/**
 初始化 SDK
 
 @param number 产品编号
 */
+ (void)initWithProductNumber:(NSString *)number completeHandler:(initBlock)block;


/**
 获取Token，默认超时3s，
 
 @param block 获取Token完成后回调
 */
+ (void)getTokenWithCompleteHandler:(tokenBlock)block;

/**
 获取Token
 
 @param block 获取token的block
 @param timeout 超时时间（范围：10000ms以内  单位:ms）

 */
+ (void)getTokenWithTimeout:(NSInteger)timeout completeHandler:(tokenBlock)block;


/**
 获取 dToken。默认超时3s
 
 @param block 获取Token完成后回调
 */
+ (void)getNdInfoWithCompleteHandler:(NdInfoBlock)block;

/**
 异步获取 dToken，并指定超时时间
 
 @param timeout 超时时间（范围：10000ms以内  单位:ms）
 @param block 获取Token完成后回调
 */
+ (void)getNdInfoWithTimeout:(NSInteger)timeout completeHandler:(NdInfoBlock)block;

/**
  传感器数据是否采集开关 默认为开启。 YES为开启，NO为关闭. 与设置属性功能相同，提供一个类方法。
 */
+ (void)setSeniorCollectStatus:(BOOL)isOpen;

/**
 设置自定义数据，可调用多次
 
 @param value 值
 @param key 键
 */
+ (void)setExtraData:(NSString *)value forKey:(NSString *)key;

/**
 设置跟踪ID
 
 @param customTrackId  跟踪ID

 */
+ (void)setCustomTrackId:(NSString *)customTrackId;

/**
 获取当前SDK版本号
 */
+ (NSString *)getSDKVersion;

@end
