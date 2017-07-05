package com.go2going;

import com.go2going.config.ApiPropModel;
import com.go2going.config.ProfPropModel;
import com.go2going.interfaceApi.ApiAnnotation;
import com.go2going.utils.PackageScan;
import com.go2going.websocket.BuissnesWebSocketServiceImpl;
import com.go2going.websocket.WebSocketService;
import com.go2going.websocket.WebSoketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by BlueT on 2017/7/4.
 */
@Component
public class ApplicationStartUp implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartUp.class);
    private  final Map<String, Class> apiMap = new HashMap<>();

    @Resource
    private ProfPropModel profPropModel;

    @Resource
    private ApiPropModel apiPropModel;

    /**
     * 订阅消息处理类,用于处理WebSocket服务返回的消息
     */
    @Resource
    private WebSocketService webSocketService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("Enter application startUp init");
        initApiMap();

        //启动websocket
        startUpWebSocket();
        LOGGER.info("Exit application startUp init");
    }

    private void startUpWebSocket() {

        // apiKey 为用户申请的apiKey
        String apiKey = profPropModel.getApiKey();

        // secretKey为用户申请的secretKey
        String secretKey = profPropModel.getSecretKey();

        String url = profPropModel.getUrl();


        // WebSocket客户端
        WebSoketClient client = new WebSoketClient(url, webSocketService);

        // 启动客户端
        client.start();

        // 添加订阅
        String[] channel = apiPropModel.getChannels();
        for (String s : channel) {
            client.addChannel(s);
        }
    }

    /**
     * 将注解的api和类进行对应
     */
    private void initApiMap(){
        Set<Class<?>> classes = PackageScan.getClasses("com.go2going.interfaceApi");

        classes.stream().filter(aClass -> aClass.isAnnotationPresent(ApiAnnotation.class)).forEach(aClass ->{
            ApiAnnotation annotation = aClass.getAnnotation(ApiAnnotation.class);
            String api = annotation.api();
            apiMap.put(api, aClass);
            try {
                Field sbj = aClass.getSuperclass().getDeclaredField("sbj");
                sbj.setAccessible(true);
                sbj.set(null,api);//sbj属性赋值
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.error(e.getMessage());
            }
        });

    }

    /**
     * 返回api类
     * @param apiName
     * @return
     */
    public Class getApiClass(String apiName) {
        return apiMap.get(apiName);
    }

}

