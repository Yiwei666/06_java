# 1. 项目功能

1. 列出运行在指定端口上的进程ID
2. 结束指定ID的进程
3. 点击启动按钮执行指定命令，点击关闭按钮，捕获运行在指定端口上的进程ID并结束


# 2. 文件结构

```
CalculatorGUI.java                       # 显示一个UI界面，点击其中的按钮可以执行按钮关联的命令
ListProcessesByPort.java                 # 列出运行在指定端口的进程ID
TerminateProcess.java                    # 结束指定ID的进程
ListAndTerminateProcess.java             # 列出指定端口的进程ID，并结束该进程
V2RayExecutorGUI.java                    # 显示一个UI界面，点击其中的按钮，会后台隐式启动新的进程来执行 V2Ray 命令


```


# 3. 环境配置
