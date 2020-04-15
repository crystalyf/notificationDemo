package tokyo.urbanlife.notificationdemo.bean;

import java.io.Serializable;

/**
 * Created by xjc on 2019/09/37.
 */
public class PushInfo implements Serializable {

    private String contentTitle;
    private String contentMessage;
    private String thumbnailImageUrl;

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }
}
