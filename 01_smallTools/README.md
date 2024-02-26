# 1. 项目功能

v2ray路线切换GUI界面，具体功能如下：

1. 列出运行在指定端口上的进程ID
2. 结束指定ID的进程
3. 点击启动按钮执行指定命令，点击关闭按钮捕获运行在指定端口上的进程ID并结束


# 2. 文件结构

```
CalculatorGUI.java                           # 显示一个UI界面，点击其中的按钮可以执行按钮关联的v2ray命令
ListProcessesByPort.java                     # 列出运行在指定端口的进程ID
TerminateProcess.java                        # 结束指定ID的进程
ListAndTerminateProcess.java                 # 列出指定端口的进程ID，并结束该进程
V2RayExecutorGUI.java                        # 显示一个UI界面，点击其中的按钮，会后台隐式启动新的进程来执行 V2Ray 命令
V2RayCommandExecutorWithTermination.java     # 相比于V2RayExecutorGUI.java 程序，添加00按钮，用于捕捉1080端口上运行的进程ID并结束
V2RayExecutorWithOutputGUI.java              # 相比于V2RayExecutorGUI.java 程序，将V2Ray程序的标准输出和错误输出重定向到名为 "v2ray_output.txt" 和 "v2ray_error.txt" 的文件中
V2RayCommandExecutorWithTerminationTXT.java  # 相比于V2RayCommandExecutorWithTermination.java对于按钮关联命令的硬编码，该程序实现读取同级目录下txt文件中的命令并执行
v2rayCommands.txt                            # 存储命令的文本，对于路径，推荐使用正斜杠(/)分隔，因为反斜杠会被java转义，需要使用双反斜杠(\\)来表示实际的单斜杠
```

- 注意：在Windows系统中，文件路径可以使用正斜杠（/）或双反斜杠（\）表示。在你提供的两个命令中，一个使用了正斜杠，另一个使用了双反斜杠。在java中，双反斜杠(\\)表示每个反斜杠都被另一个反斜杠转义，以确保它们被正确地解释为路径而不是转义字符。这样的转义是为了告诉编译器这些反斜杠是普通的字符，而不是转义字符。

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


### 3. 命令行分割

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
                String[] commandArray = command.split(" ");
                System.out.println("拆分后的命令数组: ");
                for (String part : commandArray) {
                    System.out.println(part);
                }                
                ProcessBuilder processBuilder = new ProcessBuilder(commandArray);
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

- 首先，它会检查是否存在当前正在运行的进程（currentProcess）。如果存在，它会终止当前进程并等待其终止。接着，它会输出当前进程的ID，并执行传入的V2Ray命令。代码使用一个新的线程来执行这个命令，以确保不会阻塞主线程。
- 在新线程中，它会拆分传入的命令字符串，创建一个进程构建器（ProcessBuilder），并启动一个新的子进程来执行V2Ray命令。最后，它输出新进程的ID，并等待子进程的结束，打印子进程的退出码。

注意：**如果命令参数之间使用了多个连续空格分隔，使用 split(" ") 方法可能会导致问题，因为它只会按照单个空格进行分割。**在这种情况下，你可能会得到空字符串作为数组的一部分，这可能会影响到你的命令执行。

```java
String[] commandArray = command.split("\\s+", -1);
```

在这种情况下，你可以使用正则表达式` \\s+ `分割字符串，并且在括号中传入一个正整数，表示最大分割次数。这将防止在末尾产生空字符串。

### 4. V2RayCommandExecutorWithTerminationTXT.java

通过jpackage命令转成exe文件并安装后，需要将`v2rayCommands.txt`文件拷贝到在安装目录下，通常默认为`C:\Program Files\MyV2rayTXT`。

- v2rayCommands.txt

```cmd
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c D:/software/09_v2ray/v2ray-windows-64-v5.4/config_0311_do3-1.json
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c D:/software/09_v2ray/v2ray-windows-64-v5.4/config_0311_do3-2.json
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c D:/software/09_v2ray/v2ray-windows-64-v5.4/config_0303_do1-1.json
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c D:/software/09_v2ray/v2ray-windows-64-v5.4/config_0303_do1-2.json
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c D:/software/09_v2ray/v2ray-windows-64-v5.4/config_0331_aws1-2.json
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c D:/software/09_v2ray/v2ray-windows-64-v5.4/config_1111_aws1-3.json
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c D:/software/09_v2ray/v2ray-windows-64-v5.4/config_0525_azure1-1.json
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c D:/software/09_v2ray/v2ray-windows-64-v5.4/config_0526_azure2-1.json
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c D:/software/09_v2ray/v2ray-windows-64-v5.4/config_0530_azure5-1.json
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c  D:/software/09_v2ray/v2ray-windows-64-v5.4/config_0530_azure6-1.json
D:/software/09_v2ray/v2ray-windows-64-v5.4/v2ray.exe run -c  D:/software/09_v2ray/v2ray-windows-64-v5.4/config_1124_cc1-1.json
```

# 4. 参考资料

- [java程序打包和部署](https://github.com/Yiwei666/06_java/wiki/02_Java%E7%A8%8B%E5%BA%8F%E6%89%93%E5%8C%85%E5%92%8C%E9%83%A8%E7%BD%B2#1-java%E7%A8%8B%E5%BA%8F%E6%89%93%E5%8C%85%E5%92%8C%E9%83%A8%E7%BD%B2)
- [vs code中java的配置](https://github.com/Yiwei666/12_blog/wiki/03_Visual-Studio-Code%E9%85%8D%E7%BD%AE#6-java)




















