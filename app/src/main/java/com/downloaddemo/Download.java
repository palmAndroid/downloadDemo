package com.downloaddemo;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class Download {

    /**
     * {
     *       "box_id": "ST17290248",
     *       "piece_id": "JD011100003654964939",
     *       "label": "",
     *       "shipping_date": "2021-10-27T15:53:29",
     *       "update_count": 1,
     *       "cross_check_time": "2021-10-28T19:06:57.962Z"
     *     }
     */
    @NonNull
    private int row_id ;

    @SerializedName("box_id")
    private String box_id;

    @SerializedName("piece_id")
    private String piece_id;

    @SerializedName("label")
    private String label;

    @SerializedName("cross_check_time")
    private String cross_check_time;

    @SerializedName("print_time")
    private String print_time;

    @SerializedName("print_count")
     private int print_count;

    @SerializedName("update_count")
    private int update_count;

    @SerializedName("shipping_date")
    private String shipping_date;

    private String last_updated;

    private String created_time ;

   private String last_print_updated;


    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time ;
    }

    public String getCross_check_time() {
        return cross_check_time;
    }

    public void setCross_check_time(String cross_check_time) {
        this.cross_check_time = cross_check_time;
    }

    public String getPrint_time() {
        return print_time;
    }

    public void setPrint_time(String print_time) {
        this.print_time = print_time;
    }

    public int getPrint_count() {
        return print_count;
    }

    public void setPrint_count(int print_count) {
        this.print_count = print_count;
    }

    public Download() {

    }


    public Download(int rowId,String boxId, String pieceId, String label) {
        this.row_id = rowId;
        this.box_id = boxId;
        this.piece_id = pieceId;
        this.label = label;
    }


    public String getBox_id() {
        return box_id;
    }

    public void setBox_id(String box_id) {
        this.box_id = box_id;
    }

    public String getPiece_id() {
        return piece_id;
    }

    public void setPiece_id(String piece_id) {
        this.piece_id = piece_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getRow_id() {
        return row_id;
    }

    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }

    public int getUpdate_count() {
        return update_count;
    }

    public void setUpdate_count(int update_count) {
        this.update_count = update_count;
    }


    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getLast_print_updated() {
        return last_print_updated;
    }

    public void setLast_print_updated(String last_print_updated) {
        this.last_print_updated = last_print_updated;
    }

    public String getShipping_date() {
        return shipping_date;
    }

    public void setShipping_date(String shipping_date) {
        this.shipping_date = shipping_date;
    }

}
