package com.zhiwei.park.service;

import java.util.List;
import com.zhiwei.park.domain.Contact;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.dto.MemberLoginDto;
import com.zhiwei.park.domain.vo.MemberLoginVo;

/**
 * 企业联系人Service接口
 * 
 * @author ypf
 * @date 2026-10-30
 */
public interface IContactService extends IService<Contact>
{
    /**
     * 查询企业联系人
     * 
     * @param id 企业联系人主键
     * @return 企业联系人
     */
    public Contact selectContactById(Long id);

    /**
     * 查询企业联系人列表
     * 
     * @param contact 企业联系人
     * @return 企业联系人集合
     */
    public List<Contact> selectContactList(Contact contact);

    /**
     * 新增企业联系人
     * 
     * @param contact 企业联系人
     * @return 结果
     */
    public int insertContact(Contact contact);

    /**
     * 修改企业联系人
     * 
     * @param contact 企业联系人
     * @return 结果
     */
    public int updateContact(Contact contact);

    /**
     * 批量删除企业联系人
     * 
     * @param ids 需要删除的企业联系人主键集合
     * @return 结果
     */
    public int deleteContactByIds(Long[] ids);

    /**
     * 删除企业联系人信息
     *
     * @param id 企业联系人主键
     * @return 结果
     */
    public int deleteContactById(Long id);

    /**
     * 微信小程序登录
     *
     * @param dto 登录入参
     * @return 登录结果（令牌、昵称）
     */
    public MemberLoginVo login(MemberLoginDto dto);
}
