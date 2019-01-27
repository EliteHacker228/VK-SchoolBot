package entities;


import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VKGroupMessage {

    @SerializedName("date")
    @Expose
    private Integer date;

    @SerializedName("from_id")
    @Expose
    private Integer from_id;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("out")
    @Expose
    private Integer out;

    @SerializedName("peer_id")
    @Expose
    private Integer peer_id;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("conversation_message_id")
    @Expose
    private Integer conversationMessageId;

    @SerializedName("fwd_messages")
    @Expose
    private List<java.lang.Object> fwdMessages = new ArrayList<Object>();

    @SerializedName("important")
    @Expose
    private Boolean important;

    @SerializedName("random_id")
    @Expose
    private Integer random_id;

    @SerializedName("attachments")
    @Expose
    private List<java.lang.Object> attachments = new ArrayList<Object>();

    @SerializedName("is_hidden")
    @Expose
    private Boolean is_hidden;

    /**
     * No args constructor for use in serialization
     *
     */
    public VKGroupMessage() {
    }

    /**
     *
     * @param is_hidden
     * @param id
     * @param from_id
     * @param text
     * @param random_id
     * @param conversationMessageId
     * @param fwdMessages
     * @param peer_id
     * @param important
     * @param attachments
     * @param date
     * @param out
     */
    public VKGroupMessage(Integer date, Integer from_id, Integer id, Integer out, Integer peer_id, String text, Integer conversationMessageId, List<java.lang.Object> fwdMessages, Boolean important, Integer random_id, List<java.lang.Object> attachments, Boolean is_hidden) {
        super();
        this.date = date;
        this.from_id = from_id;
        this.id = id;
        this.out = out;
        this.peer_id = peer_id;
        this.text = text;
        this.conversationMessageId = conversationMessageId;
        this.fwdMessages = fwdMessages;
        this.important = important;
        this.random_id = random_id;
        this.attachments = attachments;
        this.is_hidden = is_hidden;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getFrom_id() {
        return from_id;
    }

    public void setFrom_id(Integer from_id) {
        this.from_id = from_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOut() {
        return out;
    }

    public void setOut(Integer out) {
        this.out = out;
    }

    public Integer getPeer_id() {
        return peer_id;
    }

    public void setPeer_id(Integer peer_id) {
        this.peer_id = peer_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getConversationMessageId() {
        return conversationMessageId;
    }

    public void setConversationMessageId(Integer conversationMessageId) {
        this.conversationMessageId = conversationMessageId;
    }

    public List<java.lang.Object> getFwdMessages() {
        return fwdMessages;
    }

    public void setFwdMessages(List<java.lang.Object> fwdMessages) {
        this.fwdMessages = fwdMessages;
    }

    public Boolean getImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
    }

    public Integer getRandom_id() {
        return random_id;
    }

    public void setRandom_id(Integer random_id) {
        this.random_id = random_id;
    }

    public List<java.lang.Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<java.lang.Object> attachments) {
        this.attachments = attachments;
    }

    public Boolean getIs_hidden() {
        return is_hidden;
    }

    public void setIs_hidden(Boolean is_hidden) {
        this.is_hidden = is_hidden;
    }

}