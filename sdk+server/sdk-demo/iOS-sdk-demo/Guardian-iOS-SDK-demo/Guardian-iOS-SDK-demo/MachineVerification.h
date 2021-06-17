//
//  MachineVerification.h
//  NTESCSGuardianDemo
//
//  Created by NetEase on 16/11/7.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MachineVerification : NSObject



+ (void)isMachine:(NSString *)url token:(NSString *)token bussinessKey:(NSString *)bussinessKey secretId:(NSString *)secretId secretKey:(NSString *)secretKey;

@end
