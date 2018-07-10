package com.ycmm.model.wx;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by jishubu on 2018/7/9.
 */
public class MediaIdMessage {

    @XStreamAlias("MediaId")
    @XStreamCDATA
    private String MediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}
