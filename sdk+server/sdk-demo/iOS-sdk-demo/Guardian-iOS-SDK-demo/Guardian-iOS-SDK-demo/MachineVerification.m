//
//  MachineVerification.m
//  NTESCSGuardianDemo
//
//  Created by NetEase on 16/11/7.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import "MachineVerification.h"
#import <CommonCrypto/CommonDigest.h>


@implementation MachineVerification

+ (void)isMachine:(NSString *)urlStr token:(NSString *)token bussinessKey:(NSString *)bussinessKey secretId:(NSString *)secretId secretKey:(NSString *)secretKey {
    if(!urlStr || !token || !bussinessKey) {
        return ;
    }
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url
                                                           cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
                                                       timeoutInterval:10.0];
    NSString *time = [NSString stringWithFormat:@"%@", [NSNumber numberWithLongLong:[[NSDate date] timeIntervalSince1970]]];
    NSString *nonce = [self getnonce];
    // 生成签名
    // 1、businessId 2、nonce 3、secretId 4、timestamp 5、token 6、version
    NSString *sig = [NSString stringWithFormat:@"businessId%@nonce%@secretId%@timestamp%@token%@version%@%@",
                     bussinessKey,
                     nonce,
                     secretId,
                     time,
                     token,
                     @"300",
                     secretKey];
    NSString *md5 = [self MD5:sig];
    //NSLog(@"sig string: %@ - %@", sig, md5);
    
    NSString *postData = [NSString stringWithFormat:@"version=%@&secretId=%@&businessId=%@&timestamp=%@&nonce=%@&token=%@&signature=%@",
                                 [self urlEncode_CS:@"300"],
                                 [self urlEncode_CS:secretId],
                                 [self urlEncode_CS:bussinessKey],
                                 [self urlEncode_CS:time],
                                 [self urlEncode_CS:nonce],
                                 [self urlEncode_CS:token],
                                 [self urlEncode_CS:[md5 lowercaseString]]];
    //NSLog(@"post params: %@", postData);
    
    NSData *reportData = [postData dataUsingEncoding:NSUTF8StringEncoding];
    NSString *postLength = [NSString stringWithFormat:@"%lu", (unsigned long)[reportData length]];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPMethod:@"POST"];
    [request setHTTPBody:reportData];
    
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
        NSURLSession *session = [NSURLSession sessionWithConfiguration:configuration delegate:nil delegateQueue:nil];
        NSURLSessionDataTask *postDataTask = [session dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
    
            if(error) {
                NSLog(@"error: %@", error);
                
            }
            
            
            NSString *logFilePath = [NSTemporaryDirectory() stringByAppendingPathComponent:@"log_qa_mm"];
            NSMutableString *contents = [[NSMutableString alloc] init];
            NSString *content = [NSString stringWithContentsOfFile:logFilePath encoding:NSUTF8StringEncoding error:nil];
            if(content) {
                [contents appendString:content];
            }
            [contents appendString:@"\n==============================\n"];
            [contents appendString:[[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding]];
            [contents writeToFile:logFilePath atomically:YES encoding:NSUTF8StringEncoding error:nil];
            
            id json = [self jsonStringDecoded:data];
            NSLog(@"received json: %@", json);
            
            
        }];
        
        [postDataTask resume];
    
 
    return ;
}

+ (NSString *)getnonce {
    CFUUIDRef sUUID = CFUUIDCreate(NULL);
    CFStringRef sUUIDStringRef = CFUUIDCreateString(NULL, sUUID);
    CFRelease(sUUID);
    NSString *sUUIDString = (__bridge NSString *)sUUIDStringRef;
    sUUIDString = [sUUIDString stringByReplacingOccurrencesOfString:@"-" withString:@""];
    CFRelease(sUUIDStringRef);
    return sUUIDString;
}

+ (NSString *)urlEncode_CS:(NSString *)string {
    if(!string || ![string length]) {
        return nil;
    }
    
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
    CFStringEncoding cfEncoding = CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding);
    NSString *encoded = (__bridge_transfer NSString *)
    CFURLCreateStringByAddingPercentEscapes(
                                            kCFAllocatorDefault,
                                            (__bridge CFStringRef)string,
                                            NULL,
                                            CFSTR("!#$&'()*+,/:;=?@[]"),
                                            cfEncoding);
    return encoded;
#pragma clang diagnostic pop
}

+ (id)jsonStringDecoded:(NSData *)data {
    if(!data) {
        return nil;
    }
    
    NSError *error = nil;
    id jsonObject = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    if(jsonObject) {
        return jsonObject;
    }
    
    return nil;
}

+(NSString *)MD5:(NSString *)str{
    const char *cStr = [str UTF8String];
    unsigned char result[16];
    CC_MD5( cStr, (CC_LONG)strlen(cStr), result );
    return [NSString stringWithFormat:
            @"%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X",
            result[0], result[1], result[2], result[3],
            result[4], result[5], result[6], result[7],
            result[8], result[9], result[10], result[11],
            result[12], result[13], result[14], result[15]];
}

@end
