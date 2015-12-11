package ru.onyanov.geni.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sizes {

    @SerializedName("large")
    @Expose
    private String large;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("small")
    @Expose
    private String small;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("print")
    @Expose
    private String print;
    @SerializedName("thumb2")
    @Expose
    private String thumb2;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     *
     * @return
     * The large
     */
    public String getLarge() {
        return large;
    }

    /**
     *
     * @param large
     * The large
     */
    public void setLarge(String large) {
        this.large = large;
    }

    /**
     *
     * @return
     * The medium
     */
    public String getMedium() {
        return medium;
    }

    /**
     *
     * @param medium
     * The medium
     */
    public void setMedium(String medium) {
        this.medium = medium;
    }

    /**
     *
     * @return
     * The small
     */
    public String getSmall() {
        return small;
    }

    /**
     *
     * @param small
     * The small
     */
    public void setSmall(String small) {
        this.small = small;
    }

    /**
     *
     * @return
     * The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     *
     * @param thumb
     * The thumb
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    /**
     *
     * @return
     * The print
     */
    public String getPrint() {
        return print;
    }

    /**
     *
     * @param print
     * The print
     */
    public void setPrint(String print) {
        this.print = print;
    }

    /**
     *
     * @return
     * The thumb2
     */
    public String getThumb2() {
        return thumb2;
    }

    /**
     *
     * @param thumb2
     * The thumb2
     */
    public void setThumb2(String thumb2) {
        this.thumb2 = thumb2;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}