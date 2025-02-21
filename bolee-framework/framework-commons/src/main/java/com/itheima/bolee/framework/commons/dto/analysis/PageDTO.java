package com.itheima.bolee.framework.commons.dto.analysis;

import lombok.Data;

import java.util.Map;

/**
 * PageDTO
 *
 * @author: wgl
 * @describe: 分页对象
 * @date: 2022/12/28 10:10
 */
@Data
public class PageDTO {

    private Integer pageNum;

    private Integer pageSize = 5000;

    private Integer totalPageNum;

    private Integer totalCount;

    public PageDTO(Integer totalCount) {
        this.totalCount = totalCount;
        this.totalPageNum = (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 获取分页信息
     *
     * @param totalData
     * @return
     */
    public static PageDTO getPageInfo(Map totalData) {
        Integer count = Double.valueOf(totalData.get("count").toString()).intValue();
        PageDTO pageDTO = new PageDTO(count);
        return pageDTO;
    }
}
