# 基于钛极OS(TiJOS)的中国移动OneNET接入案例

中国移动物联网开放平台OneNET是中移物联网有限公司基于物联网技术和产业特点打造的开放平台和生态环境，适配各种网络环境和协议类型，支持各类传感器和智能硬件的快速接入和大数据服务，提供丰富的API和应用模板以支持各类行业应用和智能硬件的开发，能够有效降低物联网应用开发和部署成本，满足物联网领域设备连接、协议适配、数据存储、数据安全、大数据分析等平台级服务需求。

OneNET Studio 是新一代物联网中台，向下接入设备，向上承载应用，为用户提供一站式“终端-平台-应用”整体解决方案，帮助企业实现海量设备的快速上云。设备侧提供物联网设备接入、设备管理、数据解析、数据转发、设备运维监控等服务；应用侧提供丰富的API接口、数据推送、消息队列、规则引擎场景联动等功能；同时提供语音通话、LBS定位、数据分析及可视化等增值服务。

更多详情请访问：https://open.iot.10086.cn/doc/iot_platform/



## 代码说明

源码请参考  https://github.com/TiJOSApp/tijos-mqtt-onenet-iot

## 准备工作

在设备接入前需要在OneNET Stduio中创建相应的产品并定义其物模型

### 创建产品及物模型

从OneNET Stdudio下面的设备接入与管理中的产品管理选择添加产品

![image-20210111104137694](.\img\onenet_studio\image-20210111104137694.png)

在弹出的产品信息窗口中根据实际需要添加选择产品类别以及是否使用标准物模型， 接入协议选择MQTT,数据协议为OneJson

![image-20210111104655212](.\img\onenet_studio\image-20210111104655212.png)

创建成功后， 可进入产品详情进行物模型管理

![image-20210111105147928](.\img\onenet_studio\image-20210111105147928.png)

可选择设置物模型来进行定义功能点相关的功能点，每个功能点对应的OneJson里的相应字段

![image-20210111111822692](.\img\onenet_studio\image-20210111111822692.png)



完成所有功能点定义后，即可进行设备管理了。

![image-20210111112017939](.\img\onenet_studio\image-20210111112017939.png)



### 创建设备

在完成产品定义之后在设备管理页进行进行添加设备操作

![image-20210111112629385](.\img\onenet_studio\image-20210111112629385.png)

添加设备时设备名称建议使用TiGW200设备的IMEI号，IMEI是唯一编号并且在程序中直接获取使用， 无需进行外部设置。

![image-20210111112553506](.\img\onenet_studio\image-20210111112553506.png)

完成设备添加后即可进行设备接入操作。 

### 设备接入安全密钥

系统为每个产品分配了唯的产品ID， 并为设备分配了设备密钥

每个设备的产品ID，产品或设备密钥，设备名称构成的设备接入平台的安全密钥，在实际应用中，一般选择设备的IMEI， 产品ID及产品密钥用于安全认证登录。

![image-20210111113518451](.\img\onenet_studio\image-20210111113518451.png)

### 设备日志及在线调试

在设备详情页面中能够看到设备当前属性值， 并可选择设备日志或在线调试功能进行设备诊断,  设备日志可以用来查看设备相关消息，用于分析诊断当前设备工作状态。 

![image-20210111134709885](.\img\onenet_studio\image-20210111134709885.png)

设备调试可以用来模拟设备数据上报以及下发指令

![image-20210111134901151](.\img\onenet_studio\image-20210111134901151.png)



## 通过钛极OS(TiJOS)接入

钛极OS是钛云物联开发的物联网操作系统 ，可运行于单片机、物联网模组等低资源设备中， 支持用户通过Java语言进行进行硬件功能开发，并提供了各种云端接入组件包， 并内置支持OneNET Studio物联网平台接入。 

### 准备工作

1. 准备一台内置钛极OS(TiJOS)的设备， 建议使用支持4G的TiGW200边缘计算网关
2. 安装Eclipse及TiStudio开发环境， 具体请参考TiGW200开发指南文档或访问<钛极OS文档中心>[http://doc.tijos.net]
3. 将TiGW200进入开发模式并连接电脑USB口
4. 在OneNET studio平台中建立新设备并获取到设备的密钥认证信息， 包括产品ID， 设备名称，产品密钥

### 应用开发

在Ecclipse中新建TiJOS Application应用，腾讯云平台接入通过钛极OS(TiJOS)内置的OneNetMqttClient类及IOneNetMqttEventListener事件进行支持，用户可参考相关文档和例程接合实际应用进行开发，并通过编译下载到TiGW200设备中进行测试。 

具体可参考[中国移动OneNET物联网平台客户端 - 文档中心 (tijos.net)](http://doc.tijos.net/docstore/tijos-development-guide/tijos.framework.networkcenter.onenet/)

### 代码编译下载

从GitHub下载已完成的代码，通过Eclipse导入到Workspace中, 在Eclipse中可以看到工程基于TiJOS Framework开发，所有源码和API都是Java代码，TiJOS Framework对各种外设传感器及网络做了抽象封装，通过API可方便快捷的操作外设硬件。

在完成代码修改后，通过选中工程右键弹出菜单点击Run as --> TiJOS Application实时下载至硬件中运行。 

### 代码解析

#### 启动4G网络

在程序启动时， 先启动4G网络

```java
启动4G网络 超时30秒
TiLTE.getInstance().startup(30);
```

#### 连接OneNET Studio平台

使用设备密钥信息连接云平台

```java
//设备密钥信息	
String productId = "xt0Tv3A1gs";
//设备名称使用IMEI
String deviceName = TiLTE.getInstance().getIMEI();
//产品密钥
String productKey = "T98fK6VXP1WzkxeHMYMl38Hl4Suf3XkXFYv30/ElYzs=";

//OneNET Studio平台客户端
OneNetMqttClient onenet = new OneNetMqttClient(productId, deviceName, productKey);

//启动连接并设置事件监听
onenet.connect(new OnenetEventListner(onenet));

```

此时，可在设备的日志看到设备连接成功

#### 上报设备属性

从传感器读取当前状态，并通过OneNetMqttClient提供的属性上报接口上报相关属性， 属性必须与在平台定义的数据模型相一致

```java

//上报当前设备地理位置， 需要在物模型中加入基站定位系统功能点$OneNET_LBS
onenet.lbsCellLocationReport();

//每10秒上报属性值
OneJSONProperty property = new OneJSONProperty();
while (true) {
    //获取实际设备数据设置相应属性
    
	property.setProperty("state", System.currentTimeMillis(), 1);			
    onenet.propertyReport(property.toJSON());

    Delay.msDelay(10000);
}

```

数据上报后，可在设备的详情页面看到相关的属性值 ， 并在设备日志中看到上报的数据。

![image-20210111140054813](.\img\onenet_studio\image-20210111140054813.png)

#### 下发指令

钛极OS(TiJOS)通过事件回调的方式处理平台下发指令，用户需要在应用中实现IOneNetMqttEventListener接口, 当有属性控制指令下发时通过onPropertySetArrived来进行回调处理，并可在处理完成后通过propertySetReply回复平台， 当有服务调用下发时通过onServiceInvokeArrived进行回调处理， 并可在处理完成后通过serviceInvokeReply回复平台，如下所示：

```java
class OnenetEventListner implements IOneNetMqttEventListener {

......
	@Override
	public void onPropertySetArrived(String msgId, JSONObject params) {

		System.out.println("onPropertySetArrived " + msgId + " " + params);

		int code = 200;
		String message = "OK";
        try {
                onenet.propertySetReply(msgId, code, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
	}

	@Override
    public void onServiceInvokeArrived(String msgId, String serviceId, JSONObject params){
		System.out.println("onServiceInvokeArrived " + "msgId " + msgId + " serviceId " + serviceId + " "
				+ params.toString());

		// reply
		JSONObject response = new JSONObject();

		int code = 0;
		String status = "OK";
     	try {
			onenet.serviceInvokeReply(msgId, serviceId, 200, "OK", response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
......
}
```

## 总结

本案例实现了最基本的网络接入和收发数据以及基于基站的定位，实际中也可进一步通过OneNET Studio提供的更多功能基于设备数据进行显示、分析、转发等等与业务应用平台进行进一步的对接。