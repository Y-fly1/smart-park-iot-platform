package com.zhiwei.park.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.Contact;

/**
 * 企业联系人Mapper接口
 * 
 * @author ypf
 * @date 2026-10-30
 */
@Mapper
public interface ContactMapper extends BaseMapper<Contact>
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
     * 删除企业联系人
     * 
     * @param id 企业联系人主键
     * @return 结果
     */
    public int deleteContactById(Long id);

    /**
     * 批量删除企业联系人
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteContactByIds(Long[] ids);
}
