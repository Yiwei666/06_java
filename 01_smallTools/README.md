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


### 3. 进程管理

#### 1. V2RayCommandExecutorWithTermination.java

```java
    private void executeV2RayCommand(String command) {
        if (currentProcess != null) {
            currentProcess.destroy();
            try {
                currentProcess.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            String[] commandArray = command.split(" ");
            ProcessBuilder processBuilder = new ProcessBuilder(commandArray);
            currentProcess = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

- 这段代码是一个用于执行 V2Ray 命令的方法。让我解释一下：

  - private void executeV2RayCommand(String command)：这是一个私有方法，用于执行 V2Ray 命令。它接受一个字符串参数 command，表示要执行的命令。

  - if (currentProcess != null)：这是一个条件语句，检查当前进程是否已经存在。

  - currentProcess.destroy()：如果当前进程存在，就销毁它。这通常是为了停止之前执行的 V2Ray 进程。

  - currentProcess.waitFor()：等待当前进程终止。这是通过捕获 InterruptedException 来处理等待过程中的中断异常。

  - `String[] commandArray = command.split(" ")`：将传入的命令字符串按空格分割，得到一个字符串数组 commandArray。这是为了将命令及其参数拆分成数组，以便传递给 ProcessBuilder。

  - ProcessBuilder processBuilder = new ProcessBuilder(commandArray)：使用拆分后的命令数组创建一个新的进程构建器。

  - `currentProcess = processBuilder.start()`：启动新的进程，执行 V2Ray 命令。新的进程被赋值给 currentProcess，以便后续可以对其进行销毁或其他操作。

  - } catch (IOException e) { e.printStackTrace(); }：捕获可能发生的 IOException 异常，并打印异常信息。这是为了处理启动进程时可能出现的 I/O 错误。



#### 2. 将子进程的输出和错误流传递给当前进程并打印进程ID

```java
    private void executeV2RayCommand(String command) {
        if (currentProcess != null) {
            System.out.println("当前进程ID: " + currentProcess.pid());
            currentProcess.destroy();
            try {
                currentProcess.waitFor();
                System.out.println("先前进程已终止。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        System.out.println("执行命令: " + command);
    
        new Thread(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
                processBuilder.inheritIO(); // 将子进程的输出和错误流传递给当前进程
                currentProcess = processBuilder.start();
    
                System.out.println("新进程ID: " + currentProcess.pid());
    
                int exitCode = currentProcess.waitFor();
                System.out.println("进程结束，退出码: " + exitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
```

- `currentProcess.destroy()` 会销毁当前 Java 进程所关联的子进程，**但并不会递归销毁子进程的子进程**。如果 V2Ray 相关的子进程有自己的子进程，这些子进程可能会继续运行。Java 的 destroy() 方法只是销毁直接子进程。

- **在你的代码中，由于你使用了 cmd /c 来执行命令，可能会有一些中间的 CMD 进程，而 V2Ray 实际的子进程可能与这个 CMD 进程有一些父子关系。这可能导致在销毁当前进程后，V2Ray 相关的子进程仍然在运行。**

- 如果要确保 V2Ray 及其所有子进程都被终止，你可能需要考虑使用更复杂的方法，例如使用系统命令或其他工具，递归地终止所有相关进程。要记住，在处理进程树时，确保你了解操作的影响，以避免不必要的副作用。
































