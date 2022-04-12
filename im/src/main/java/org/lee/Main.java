package org.lee;

import org.apache.logging.log4j.core.config.Configurator;
import org.lee.util.CustomConfigurationFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Main {
    private static final Set<String> env = new HashSet<>(10);
    private static final String[] client = {"c", "client"};
    private static final String[] server = {"s", "server"};
    private static final Map<String, String> commands = new HashMap<>(16);
    public static final Integer port = 80;

    static {
        env.addAll(Set.of("e","env","enviroment"));
    }

    public static void main(String[] args) {
        // 解析参数
        parseArgs(args);
        // 初始化日志配置
        initLog();
        // 运行客户端或者服务端
        run();
    }
    public static void initLog(){
        CustomConfigurationFactory customConfigurationFactory = new CustomConfigurationFactory();
        Configurator.initialize(customConfigurationFactory.getConfiguration());
    }

    public static void parseArgs(String[] args) {
//        if (args.length == 0) {
//            System.out.println("需要指定一下启动的环境");
//        }
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ((arg.startsWith("-") || arg.startsWith("--")) && i < args.length - 1) {
                commands.put(arg.substring(1), args[++i]);
            }
        }
        commands.forEach((k,v)-> System.out.println(k+":"+v));
    }
    public static void run(){
        String enviroment = null;
        for (String e : env) {
            enviroment = commands.get(e);
            if (enviroment!=null){
                break;
            }
        }
        runClient(enviroment);
        runServer(enviroment);
    }
    public static void runClient(String e) {
        try {

            boolean shouldRun = false;
            for (String c : client) {
                if (c.equalsIgnoreCase(e)){
                    shouldRun = true;
                    break;
                }
            }
            if (shouldRun){
                Client.run(commands.get("ip"),
                        Integer.parseInt(commands.get("port")),
                        commands.get("clientId")
                );
            }
        }catch (Exception ex){
            System.out.println("出现错了");
        }
    }
    public static void runServer(String e) {
        try {

            boolean shouldRun = false;
            for (String s : server) {
                if (s.equalsIgnoreCase(e)){
                    shouldRun = true;
                    break;
                }
            }
            if (shouldRun){
                Server.run(Integer.parseInt(commands.get("port")));
            }
        }catch (Exception ex){
            System.out.println("出现错了");
        }
    }
    public static String getArg(String key){
        return commands.get(key);
    }

}
