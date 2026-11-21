package com.zhiwei.park.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.hutool.core.util.RandomUtil;
import com.zhiwei.common.utils.DateUtils;
import com.zhiwei.common.utils.StringUtils;
import com.zhiwei.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhiwei.park.mapper.ContactMapper;
import com.zhiwei.park.domain.Contact;
import com.zhiwei.park.domain.dto.MemberLoginDto;
import com.zhiwei.park.domain.vo.MemberLoginVo;
import com.zhiwei.park.service.IContactService;
import com.zhiwei.park.util.WxMiniAppClient;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 企业联系人Service业务层处理
 * 
 * @author ypf
 * @date 2026-10-30
 */
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements IContactService
{
    /** 用户昵称前缀集合 */
    private static final List<String> NICK_NAME_PREFIX_LIST = Arrays.asList(
            "大吉大利", "今晚吃鸡", "福星高照", "好事发生",
            "匆匆忙忙", "连滚带爬", "从从容容", "游刃有余");

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private WxMiniAppClient wxMiniAppClient;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询企业联系人
     * 
     * @param id 企业联系人主键
     * @return 企业联系人
     */
    @Override
    public Contact selectContactById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询企业联系人列表
     * 
     * @param contact 企业联系人
     * @return 企业联系人
     */
    @Override
    public List<Contact> selectContactList(Contact contact)
    {
        return contactMapper.selectContactList(contact);
    }

    /**
     * 新增企业联系人
     * 
     * @param contact 企业联系人
     * @return 结果
     */
    @Override
    public int insertContact(Contact contact)
    {
        return save(contact)? 1 : 0;
    }

    /**
     * 修改企业联系人
     * 
     * @param contact 企业联系人
     * @return 结果
     */
    @Override
    public int updateContact(Contact contact)
    {
        return updateById(contact)? 1 : 0;
    }

    /**
     * 批量删除企业联系人
     * 
     * @param ids 需要删除的企业联系人主键
     * @return 结果
     */
    @Override
    public int deleteContactByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除企业联系人信息
     * 
     * @param id 企业联系人主键
     * @return 结果
     */
    @Override
    public int deleteContactById(Long id)
    {
        return removeById(id)? 1 : 0;
    }

    /**
     * 微信小程序登录
     *
     * @param dto 登录入参
     * @return 登录结果（令牌、昵称）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberLoginVo login(MemberLoginDto dto)
    {
        // 1. code换取openid
        String openId = wxMiniAppClient.code2Session(dto.getCode());

        // 2. 根据openid查询联系人信息
        Contact contact = lambdaQuery()
                .eq(Contact::getOpenId, openId)
                .orderByDesc(Contact::getId)
                .last("limit 1")
                .one();

        // 3. 用手机号凭证换取手机号
        String phone = wxMiniAppClient.getPhoneNumber(dto.getPhoneCode());

        String nickName;
        if (contact == null)
        {
            // 4. 联系人不存在则新增，昵称为四字前缀+手机号后四位
            nickName = generateNickName(phone);
            contact = new Contact();
            contact.setOpenId(openId);
            contact.setPhone(phone);
            contact.setName(nickName);
            contact.setCreateTime(DateUtils.getNowDate());
            save(contact);
        }
        else
        {
            // 5. 联系人已存在，手机号不一致则更新
            nickName = StringUtils.isNotEmpty(contact.getName())
                    ? contact.getName() : generateNickName(phone);
            boolean phoneChanged = !phone.equals(contact.getPhone());
            boolean nameChanged = !nickName.equals(contact.getName());
            if (phoneChanged || nameChanged)
            {
                contact.setPhone(phone);
                contact.setName(nickName);
                contact.setUpdateTime(DateUtils.getNowDate());
                updateById(contact);
            }
        }

        // 6. 基于联系人id和昵称生成token
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", contact.getId());
        paramMap.put("nickName", nickName);
        String token = tokenService.createToken(paramMap);

        MemberLoginVo vo = new MemberLoginVo();
        vo.setToken(token);
        vo.setNickName(nickName);
        return vo;
    }

    /**
     * 生成随机昵称：四字前缀 + 手机号后四位
     *
     * @param phone 手机号
     * @return 昵称
     */
    private String generateNickName(String phone)
    {
        List<String> nickNameList = new ArrayList<>(NICK_NAME_PREFIX_LIST);
        Collections.shuffle(nickNameList);
        String prefix = nickNameList.get(0);
        String postfix = StringUtils.isNotEmpty(phone) && phone.length() >= 4
                ? phone.substring(phone.length() - 4)
                : RandomUtil.randomNumbers(4);
        return prefix + postfix;
    }
}
