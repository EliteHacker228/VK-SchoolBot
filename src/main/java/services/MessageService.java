package services;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import entities.VKGroupMessage;
import entities.VKRequest;

public class MessageService {
    private TransportClient transportClient;
    private VkApiClient vk;
    private VKRequest vkRequest;
    private VKGroupMessage vkGroupMessage;
    private GroupActor actor;

    public MessageService(VKRequest vkRequest){
        this.vkRequest=vkRequest;
        actor = new GroupActor(177305058, "6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e");
        vkGroupMessage=vkRequest.getObject();
        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
    }

    public void sendMessage(String text){
        try {
            vk.messages().send(actor).userId(vkRequest.getObject().getFrom_id()).message("Бот проверка").execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
