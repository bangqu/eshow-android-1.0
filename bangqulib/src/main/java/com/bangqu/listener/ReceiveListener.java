package com.bangqu.listener;

/***
 * 
 * @项目名:DailyFashion  
 * 
 * @类名:ReceiveListener.java  
 * 
 * @创建人:shibaotong 
 *
 * @类描述:Fragment接收数据接口
 * 
 * @date:2015-9-1
 * 
 * @Version:1.0 
 *
 *****************************************
 */
public interface ReceiveListener {
    
    void Receive(String requestname, String response);

}
