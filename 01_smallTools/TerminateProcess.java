import java.io.IOException;

public class TerminateProcess {
    public static void main(String[] args) {
        // 替换为要终止的进程的ID
        int processIdToTerminate = 23420;

        try {
            // 终止进程
            terminateProcess(processIdToTerminate);

            // 可以添加一些日志或其他处理来指示进程已经被终止
            System.out.println("Process terminated gracefully.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void terminateProcess(int processId) throws IOException, InterruptedException {
        // 构建操作系统命令
        String os = System.getProperty("os.name").toLowerCase();

        String command;
        if (os.contains("win")) {
            // Windows
            command = "taskkill /F /PID " + processId;
        } else {
            // Unix/Linux/Mac
            command = "kill -9 " + processId;
        }

        // 执行命令
        Process process = Runtime.getRuntime().exec(command);

        // 等待命令执行完毕
        process.waitFor();
    }
}
