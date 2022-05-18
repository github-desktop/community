package com.laxi.pojo;

import lombok.Data;

@Data
public class Page {
    private int current = 1;
    private int limit = 10;
    private int rows;
    private String path;

    public int getOffset() {
        return (current - 1) * limit;
    }

    /*
    用来获取总的页数
     */
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /*
    返回起始页码
     */
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /*
    获取终止页码
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
