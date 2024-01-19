# 1. 项目功能

使用JavaFX创建GUI界面，实现输入网址访问相应网页、播放视频等功能

# 2. 项目结构

```
Main.java                                     # JavaFX调用的主程序
JavaFX_Sample.java                            # GUI界面中输出hello world的测试子程序
SimpleWebBrowserJavaFX.java                   # GUI界面中实现输入网址访问网页
Socks5ProxyExample.java                       # 使用本地1080端口的socks5代理访问网页
```


# 3. 环境配置

1. JavaFX配置

参考：[vscode 中JavaFX的安装和配置](https://github.com/Yiwei666/12_blog/wiki/03_Visual-Studio-Code%E9%85%8D%E7%BD%AE#6-java)

2. Main.java

需要将`Application.launch(SimpleWebBrowserJavaFX.class)`中的`SimpleWebBrowserJavaFX.class`替换为对应子程序的类

```
import javafx.application.Application;
public class Main{
    public static void main(String []args){
        Application.launch(SimpleWebBrowserJavaFX.class);
    }
}
```



# 参考资料

vscode 中JavaFX的安装和配置: https://github.com/Yiwei666/12_blog/wiki/03_Visual-Studio-Code%E9%85%8D%E7%BD%AE#6-java
