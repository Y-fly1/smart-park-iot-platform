package com.zhiwei.park.controller.member;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhiwei.common.annotation.Log;
import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.common.enums.BusinessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.zhiwei.park.domain.Contact;
import com.zhiwei.park.service.IContactService;
import com.zhiwei.common.utils.poi.ExcelUtil;
import com.zhiwei.common.core.page.TableDataInfo;

/**
 * 企业联系人Controller
 * 
 * @author ypf
 * @date 2026-10-30
 */
@RestController
@RequestMapping("/park/member")
@Tag(name = "企业联系人相关接口")
public class ContactController extends BaseController
{
    @Autowired
    private IContactService contactService;

    /**
     * 查询企业联系人列表
     */
    @PreAuthorize("@ss.hasPermi('park:member:list')")
    @GetMapping("/list")
    @Operation(summary = "查询企业联系人列表")
    public TableDataInfo list(Contact contact)
    {
        startPage();
        List<Contact> list = contactService.selectContactList(contact);
        return getDataTable(list);
    }

    /**
     * 导出企业联系人列表
     */
    @PreAuthorize("@ss.hasPermi('park:member:export')")
    @Log(title = "企业联系人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出企业联系人列表")
    public void export(HttpServletResponse response, Contact contact)
    {
        List<Contact> list = contactService.selectContactList(contact);
        ExcelUtil<Contact> util = new ExcelUtil<Contact>(Contact.class);
        util.exportExcel(response, list, "企业联系人数据");
    }

    /**
     * 获取企业联系人详细信息
     */
    @PreAuthorize("@ss.hasPermi('park:member:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取企业联系人详细信息")
    public AjaxResult getInfo(@Schema(name = "企业联系人ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(contactService.selectContactById(id));
    }

    /**
     * 新增企业联系人
     */
    @PreAuthorize("@ss.hasPermi('park:member:add')")
    @Log(title = "企业联系人", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增企业联系人")
    public AjaxResult add(@RequestBody Contact contact)
    {
        return toAjax(contactService.insertContact(contact));
    }

    /**
     * 修改企业联系人
     */
    @PreAuthorize("@ss.hasPermi('park:member:edit')")
    @Log(title = "企业联系人", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改企业联系人")
    public AjaxResult edit(@RequestBody Contact contact)
    {
        return toAjax(contactService.updateContact(contact));
    }

    /**
     * 删除企业联系人
     */
    @PreAuthorize("@ss.hasPermi('park:member:remove')")
    @Log(title = "企业联系人", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除企业联系人")
    public AjaxResult remove(@Schema(name = "企业联系人ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(contactService.deleteContactByIds(ids));
    }
}
