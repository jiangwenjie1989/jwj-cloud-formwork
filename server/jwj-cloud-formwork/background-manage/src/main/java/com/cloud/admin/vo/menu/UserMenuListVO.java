package com.cloud.admin.vo.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;


@Data
public class UserMenuListVO extends MenuInfoVO{

    @ApiModelProperty(value="二级菜单")
    private List<MenuInfoVO> twoMenuList;

}
