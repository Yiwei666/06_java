# 1. 项目功能

1. 列出运行在指定端口上的进程ID
2. 结束指定ID的进程
3. 点击启动按钮执行指定命令，点击关闭按钮，捕获运行在指定端口上的进程ID并结束


# 2. 文件结构

```
CalculatorGUI.java                           # 显示一个UI界面，点击其中的按钮可以执行按钮关联的v2ray命令
ListProcessesByPort.java                     # 列出运行在指定端口的进程ID
TerminateProcess.java                        # 结束指定ID的进程
ListAndTerminateProcess.java                 # 列出指定端口的进程ID，并结束该进程
V2RayExecutorGUI.java                        # 显示一个UI界面，点击其中的按钮，会后台隐式启动新的进程来执行 V2Ray 命令
V2RayCommandExecutorWithTermination.java     # 相比于V2RayExecutorGUI.java 程序，添加00按钮，用于捕捉1080端口上运行的进程ID并结束
V2RayExecutorWithOutputGUI.java              # 相比于V2RayExecutorGUI.java 程序，将V2Ray程序的标准输出和错误输出重定向到名为 "v2ray_output.txt" 和 "v2ray_error.txt" 的文件中

```


# 3. 环境配置

### 1. V2RayExecutorWithOutputGUI.java

上述代码实现了一个简单的图形用户界面（GUI）程序，该程序包含多个按钮，每个按钮与不同的V2Ray命令关联。当用户点击按钮时，程序将启动一个新的子进程来执行相应的V2Ray命令，并将V2Ray程序的标准输出和错误输出重定向到名为 "v2ray_output.txt" 和 "v2ray_error.txt" 的文件中。这样，用户可以通过点击按钮来方便地启动和管理不同的V2Ray配置。


### 2. 编程语言及其GUI相关框架

| Programming Language | GUI Frameworks                     |
|:-----------------------:|:------------------------------------:|
| C#                    | WPF (Windows Presentation Foundation), WinForms |
| C++                   | MFC (Microsoft Foundation Classes), Qt       |
| Python                | Tkinter, PyQt, Kivy                   |
| Java                  | JavaFX, Swing                        |
| JavaScript (Web)      | Electron                             |
| Swift                 | SwiftUI (for macOS/iOS apps)         |
| Kotlin (Android)      | Android SDK                          |


### 3. 将子进程的输出和错误流传递给当前进程

```java
    private void executeV2RayCommand(String command) {
        if (currentProcess != null) {
            currentProcess.destroy();
            try {
                currentProcess.waitFor();
                System.out.println("Previous process terminated.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        System.out.println("Executing command: " + command);
    
        new Thread(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
                processBuilder.inheritIO(); // 将子进程的输出和错误流传递给当前进程
                currentProcess = processBuilder.start();
                int exitCode = currentProcess.waitFor();
                System.out.println("Process finished with exit code: " + exitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
```
