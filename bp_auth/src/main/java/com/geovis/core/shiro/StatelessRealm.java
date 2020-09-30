package com.geovis.core.shiro;

import com.geovis.core.common.exception.ForbiddenException;
import com.geovis.core.constant.AuthConstant;
import com.geovis.core.util.CacheUtil;
import com.geovis.core.util.JwtTokenUtil;
import com.geovis.core.util.StringUtil;
import com.geovis.core.util.SystemCacheUtil;
import com.geovis.web.domain.system.SysResource;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.ResourceService;
import com.geovis.web.service.system.UserService;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;
@SuppressWarnings("all")
public class StatelessRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private ResourceService resourceService;

    @Override
    public boolean supports(AuthenticationToken token) {
        /**
         * 仅支持StatelessToken 类型的Token，
         * 那么如果在StatelessAuthcFilter类中返回的是UsernamePasswordToken，那么将会报如下错误信息：
         * Please ensure that the appropriate Realm implementation is configured correctly or
         * that the realm accepts AuthenticationTokens of this type.StatelessAuthcFilter.isAccessAllowed()
         */
        return token instanceof StatelessToken;
    }

    /**
     * 认证方法
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        StatelessToken statelessToken = (StatelessToken) token;
        String username = statelessToken.getUsername();

        Claims claims = JwtTokenUtil.parseToken(((StatelessToken) token).getAuthToken(), AuthConstant.SECRETKET);

        if(username.equals(claims.getSubject())){
            //验证用户是否在缓存中，如果不存在则添加到缓存
            Object obj = CacheUtil.get(SystemCacheUtil.CACHE_USER, username);
            if(obj == null){
                SysUser sysUser = this.userService.selectUserByUsername(username);
                CacheUtil.put(SystemCacheUtil.CACHE_USER, username,sysUser);
                if(sysUser == null){
                    throw new ForbiddenException("用户不存在或已被删除！");
                }
            }
            //直接通过认证(
            return new SimpleAuthenticationInfo(
                    username,
                    ((StatelessToken) token).getAuthToken(),
                    getName());
        } else {
            return null;//throw new AuthenticationException("授权认证失败！");
        }

    }

    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //根据用户名查找角色，请根据需求实现
        String username = (String) principals.getPrimaryPrincipal();
        SysUser sysUser = (SysUser) CacheUtil.get(SystemCacheUtil.CACHE_USER, SystemCacheUtil.SYS_USER_USERNAME + username);

        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //管理员获取所有权限
        if(sysUser!=null&&"admin".equals(sysUser.getUsername())) {
        	info.addStringPermission("admin");
        	return info;
        }
        
        List<SysResource> resourcesList = this.resourceService.listResourceByUserId(sysUser.getId());
        
        if(resourcesList != null){
            for(SysResource resource: resourcesList){
                if (StringUtil.isNotBlank(resource.getPermission())){
                    //添加基于Permission的权限信息
                    for (String permission : StringUtil.split(resource.getPermission(),",")){
                        info.addStringPermission(permission);
                    }
                }
            }
        }

        return info;
    }
}
