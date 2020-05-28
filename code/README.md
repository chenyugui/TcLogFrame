#code
说明：写这个库的目的，不是为了解决`特定的某一类问题`，而是集成我们平时App开发中，大部分App都需要遇到的工具、组件，让新开发的app一依赖这个库，底层的框架就基本搭建完毕。 免去每个新建的app重复搭建框架的时间。<br>
但是注意，集成的只是基础架构、工具，绝对不要把业务逻辑相关的代码写进来。

下面我会一一介绍每个组件。


## 一. 统一位置配置思想，AppGlobal。
一个App，经常需要设置很多全局的配置项，例如Application对象、服务器API接口的BaseUrl、app主题颜色、设计图尺寸等。<br>

如果能在同一个位置，对这些配置进行设置的话，后期如果需要维护修改，直接进到这个位置快速查找修改即可，这里说的位置，一般就是App的Application类的onCreate方法里了。<br>

这里采用了单例模式的`Configurator`类来管理、存放这些数据。<br>
同时，为了方便调用，用了一个可以链式调用的`AppGlobal`类来进行数据的初始化、配置、获取。它也充当了外观类的角色哦，不过它只提供了获取`最基本最常用的`数据的方法，例如：获取Application的context对象、获取全局共用的一个handler对象。 其他不常用的数据，就调用AppGlobal.getConfiguration(ConfigType configType)去获取就行了。
<br>

有了这些类，配置项的设置和获取可以说是比较清晰方便了：

- 配置设置
```java
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppGlobal.init(this)
                .withApiHost(Config.BASE_URL)// 后台接口BaseUrl
                .withThemeColor(getResources().getColor(R.color.themeColor))// 主题颜色
                .withMatchScreen(1080f)// UI设计图分辨率的宽
                .configure();
    }
}
```

- 配置获取：
``` java
AppGlobal.getApplicationContext();
AppGlobal.getHandler();
AppGlobal.getConfiguration(ConfigType.API_HOST);
AppGlobal.getConfiguration(ConfigType.MATCH_SCREEN_DESIGN_WIDTH);
```

## 二. 屏幕适配
关于屏幕适配的文章，网上一搜有很多，这里推荐一篇比较通俗易懂的：
https://mp.weixin.qq.com/s/aWmLo3Ka1Rpqypc8n-pn7A<br>

这个库有两种适配方式可采用(推荐第二种)：
- 一是鸿洋大神的`AutoLayout`框架，BaseActivity已继承AutoLayoutActivity（如果不想用这个框架，那宽高的单位不要用px就好了）
- 二是采用修改density值的方式，使用下面代码配置设计图的宽度即可，在BaseActivity里会根据设计图宽度动态修改density值。
``` java
AppGlobal.init(this)
        .withMatchScreen(float designWidth)
        .configure();
```

另外，稍微介绍下第二种适配方式的原理：

我们都知道一个公式： `px = dp(dpi/160)`。对于手机，每款手机的px值是可得的并且不可修改的。
而dpi值，在应用层是可以修改的，那么我们的目标就是修改dpi值，让下面公式成立：
`dpi/160 = px/dp;`

如果设计稿宽度是1080px，那么开发这边就会把目标dp值设为1080dp, 即：
dpi = px/1080*160;

这样的话，就能保证UI在不同的设备上表现一致了。



## 三. 网络框架
网络框架的采用可谓是一个app最重要的一个环节。本库采用对Retrofit2进行二次封装的方式，加上builder模式，让使用更方便，代码更清晰，建议是在已熟悉Retrofit2的使用的基础上再使用。<br>
下面是一个post请求的示例：
```java
RestClient.builder()
        .loading(getContext())// 请求前开启loadingDialog，请求结束自动关闭
        .addInterceptor(new MQTTAuthorInterceptor())// 添加拦截器，http Header的添加可以通过添加拦截器来实现
        .exitPageAutoCancel(this)// 退出界面自动关闭请求
        .url(url)// 设置请求url
        .param("param1", param1)// 设置请求参数
        .param("param2", param2)// 设置请求参数
        .callback(new MQTTResultDataCallBack<Boolean>(Boolean.class) {
            @Override
            public void onSuccess(Boolean data) {
                controlSuccess(temp, mode, windSpeed);
            }

            @Override
            public void onFail(String code, String err) {
                showShort(err);
            }
        })
        .build()
        .post();// 进行post请求
```

## 四. 6.0动态权限获取
todo

## 五. 7.0 FileProvider
todo


## .监听Wifi状态的NetBroadcastReceiver
todo

## .BaseActivity、BaseFragment
- MVP
todo
- LifeCycle
todo
- fragmentation
todo

## . 其他常用工具类
todo

- EventData
todo


## 发布到github、简书、掘金吧亲
