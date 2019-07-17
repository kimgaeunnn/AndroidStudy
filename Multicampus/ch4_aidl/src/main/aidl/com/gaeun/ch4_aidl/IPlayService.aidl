// IPlayService.aidl
package com.gaeun.ch4_aidl;

// Declare any non-default types here with import statements

// AIDL로 서비스를 제공하는 앱에서 만들어서, AIDL을 이용하는 앱에게 공유되어야 함.
// 외부에서 호출하고자 하는 함수 나열
// AIDL을 만든 후 build -> make module을 진행해야함 (java에서 인지)

interface IPlayService {
    int currentPosition();
    int getMaxDuration();
    void start();
    void stop();
}
