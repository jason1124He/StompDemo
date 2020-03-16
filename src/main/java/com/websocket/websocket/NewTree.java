package com.websocket.websocket;

public class NewTree {

    private Integer deviceId;

    /**
     * 对应设备类型
     */
    private String level;

    /**
     * 节点设备状态
     */
    private Integer status;

    /**
     * 节点层级
     */
    private Integer depth;

    private Long key;

    private String text;

    private String fill;

    private Integer __gohashid;

    private String source;

    private Long parent;

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public Integer get__gohashid() {
        return __gohashid;
    }

    public void set__gohashid(Integer __gohashid) {
        this.__gohashid = __gohashid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }
}
