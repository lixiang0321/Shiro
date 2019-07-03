package com.yootk.shiro.realm;

import com.yootk.shiro.pojo.Member;
import com.yootk.shiro.service.IMemberService;
import com.yootk.shiro.service.impl.MemberServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Map;
import java.util.Set;

public class MemberRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("MemberRealm 开始准备进行授权处理了");
        IMemberService memberService = new MemberServiceImpl();
//        根据用户名查询权限
        System.out.println(principals.getPrimaryPrincipal() + "我想看看授权的参数的输出");
        Map<String, Set<String>> map = memberService.findPrivilegeByMember((String)principals.getPrimaryPrincipal());
        //将所有获取的权限信息保存在AuthorizationInfo类的实例中
        SimpleAuthorizationInfo authz = new SimpleAuthorizationInfo();//返回的授权信息
        //所有的角色集合
        authz.setRoles(map.get("allRoles"));
        //所有的权限集合
        authz.setStringPermissions(map.get("allActions"));
        return authz;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("Member 开始进行用户认证处理了");
        String mid = (String) token.getPrincipal();//获取用户名
        IMemberService memberService = new MemberServiceImpl();
        Member member = memberService.get(mid);//根据Mid进行查询
        if(member ==null){
            throw new UnknownAccountException(mid + "账户信息不存在");
        }
        String password = new String((char[]) token.getCredentials());
        if(!(member.getPassword().equals(password))){
            throw  new IncorrectCredentialsException("错误的用户名或者密码");
        }
        if(member.getLocked().equals(1)){
            throw new LockedAccountException(mid + "用户已经被锁定了");
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(),token.getCredentials(),this.getName());
    }
}
