package entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VKRequest {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("object")
    @Expose
    private VKGroupMessage object;

    @SerializedName("group_id")
    @Expose
    private Integer groupId;

    /**
     * No args constructor for use in serialization
     *
     */
    public VKRequest() {
    }

    /**
     *
     * @param groupId
     * @param object
     * @param type
     */
    public VKRequest(String type, VKGroupMessage object, Integer groupId) {
        super();
        this.type = type;
        this.object = object;
        this.groupId = groupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VKGroupMessage getObject() {
        return object;
    }

    public void setObject(VKGroupMessage object) {
        this.object = object;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}