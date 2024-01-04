import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ListAndTerminateProcess {
    public static void main(String[] args) {
        // 指定要查询的端口号
        int port = 1080;

        try {
            // 查询运行在指定端口的进程并获取其ID
            int processId = findProcessIdByPort(port);

            if (processId != -1) {
                // 终止找到的进程
                terminateProcess(processId);

                // 输出提示信息
                System.out.println("Process with ID " + processId + " terminated gracefully.");
            } else {
                System.out.println("No process found running on port " + port);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int findProcessIdByPort(int port) throws IOException, InterruptedException {
        // 构建查询进程的命令
        String command = String.format("netstat -ano | find \"LISTENING\" | find \":%d\"", port);

        // 执行命令
        Process process = new ProcessBuilder().command("cmd", "/c", command).start();

        // 获取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            // 提取进程ID
            String[] parts = line.trim().split("\\s+");
            if (parts.length >= 5) {
                return Integer.parseInt(parts[4]);
            }
        }

        // 等待命令执行完成
        process.waitFor();

        return -1; // 返回-1表示未找到匹配的进程ID
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
        Process terminateProcess = Runtime.getRuntime().exec(command);

        // 等待命令执行完毕
        terminateProcess.waitFor();
    }
}
