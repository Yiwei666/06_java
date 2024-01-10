import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class Socks5ProxyExample {

    public static void main(String[] args) {
        // 设置系统代理
        System.setProperty("java.net.useSystemProxies", "true");

        try {
            // 创建一个 URI 对象表示目标网址
            URI uri = new URI("http://www.google.com");

            // 获取系统代理选择器
            ProxySelector proxySelector = ProxySelector.getDefault();

            // 根据 URI 获取代理列表
            List<Proxy> proxyList = proxySelector.select(uri);

            // 如果存在代理，则使用第一个代理进行连接
            if (!proxyList.isEmpty()) {
                Proxy proxy = proxyList.get(0);

                // 使用本地 SOCKS5 代理打开连接
                InetSocketAddress localSocksProxy = new InetSocketAddress("127.0.0.1", 1080);
                Proxy socksProxy = new Proxy(Proxy.Type.SOCKS, localSocksProxy);

                // 创建 URL 对象表示目标网址
                URL url = new URL("http://www.google.com");

                // 使用代理打开连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(socksProxy);

                // 发送 GET 请求
                connection.setRequestMethod("GET");

                // 读取返回的信息
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }

                // 关闭连接
                connection.disconnect();
            } else {
                System.out.println("没有找到代理");
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
