package com.yootk.shiro.service;

import com.yootk.shiro.pojo.Member;

import java.util.Map;
import java.util.Set;

public interface IMemberService {
    public Member get(String mid);

    /**
     * 根据用户名获取用户对应的所有角色和所有权限的信息
     * @param mid
     * @return
     */
    public Map<String, Set<String>> findPrivilegeByMember(String mid);
}
