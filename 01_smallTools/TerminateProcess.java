import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TerminateProcess {
    public static void main(String[] args) {
        // 替换为要终止的进程的ID
        int processIdToTerminate = 20820;

        try {
            // 检查进程是否存在
            if (isProcessRunning(processIdToTerminate)) {
                // 终止进程
                terminateProcess(processIdToTerminate);

                // 可以添加一些日志或其他处理来指示进程已经被终止
                System.out.println("Process terminated gracefully.");
            } else {
                System.out.println("Process with ID " + processIdToTerminate + " does not exist.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean isProcessRunning(int processId) throws IOException, InterruptedException {
        // 构建操作系统命令
        String os = System.getProperty("os.name").toLowerCase();

        String command;
        if (os.contains("win")) {
            // Windows
            command = "tasklist /FI \"PID eq " + processId + "\"";
        } else {
            // Unix/Linux/Mac
            command = "ps -p " + processId;
        }

        // 执行命令
        Process process = Runtime.getRuntime().exec(command);

        // 等待命令执行完毕
        process.waitFor();

        // 读取命令的输出流
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // 检查命令的输出来判断进程是否存在
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(Integer.toString(processId))) {
                return true;
            }
        }

        return false;
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
