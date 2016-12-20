//
//  DataChecker.m
//  GuardianActivityDemo
//
//  Created by NetEase on 16/12/20.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import "DataChecker.h"
#import <UIKit/UIKit.h>

@implementation DataChecker

+ (void)sendData:(NSString *)token {
    if(!token) {
        return;
    }
    
    NSString *postString = [NSString stringWithFormat:@"token=%@", [self urlEncode:token]];
    
    NSURL *url = [NSURL URLWithString:@"http://localhost:8181/rise.do"];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url
                                                           cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
                                                       timeoutInterval:10.0];
    NSData *postData = [postString dataUsingEncoding:NSUTF8StringEncoding];
    NSString *postLength = [NSString stringWithFormat:@"%lu", (unsigned long)[postString length]];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPMethod:@"POST"];
    [request setHTTPBody:postData];
    
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:configuration delegate:nil delegateQueue:nil];
    NSURLSessionDataTask *postDataTask = [session dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        NSString *alertString = nil;
        if(error) {
            alertString = [NSString stringWithFormat:@"error: %@", [error description]];
        } else {
            alertString = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        }
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self showAlert:alertString];
        });
    }];
    [postDataTask resume];
}

+ (NSString *)urlEncode:(NSString *)string {
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

+ (void)showAlert:(NSString *)msg {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Notice" message:msg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    [alert show];
}

@end
