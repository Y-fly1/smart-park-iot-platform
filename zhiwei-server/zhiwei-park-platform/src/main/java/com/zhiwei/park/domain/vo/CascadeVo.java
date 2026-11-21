package com.zhiwei.park.domain.vo;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 级联树节点VO（楼栋->房间->点位）
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "级联树节点")
public class CascadeVo
{
    /** 节点值（主键ID） */
    @Schema(title = "节点值")
    private String value;

    /** 节点名称 */
    @Schema(title = "节点名称")
    private String label;

    /** 子节点 */
    @Schema(title = "子节点")
    private List<CascadeVo> children;
}
