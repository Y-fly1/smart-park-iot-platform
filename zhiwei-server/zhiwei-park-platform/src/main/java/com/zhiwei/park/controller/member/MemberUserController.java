package com.zhiwei.park.controller.member;

import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.park.domain.dto.MemberLoginDto;
import com.zhiwei.park.service.IContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序用户Controller
 *
 * @author ypf
 * @date 2026-10-30
 */
@RestController
@RequestMapping("/member/user")
@Tag(name = "小程序用户相关接口")
public class MemberUserController extends BaseController
{
    @Autowired
    private IContactService contactService;

    /**
     * 微信小程序登录
     */
    @PostMapping("/login")
    @Operation(summary = "微信小程序登录")
    public AjaxResult login(@Validated @RequestBody MemberLoginDto dto)
    {
        return success(contactService.login(dto));
    }
}
