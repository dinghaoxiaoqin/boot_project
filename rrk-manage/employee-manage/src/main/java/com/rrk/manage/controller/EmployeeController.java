package com.rrk.manage.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.modules.manage.dto.DelEmployeeDto;
import com.rrk.common.modules.manage.dto.EmployeeDto;
import com.rrk.common.modules.manage.dto.EmployeeInfo;
import com.rrk.common.modules.manage.entity.TbEmployee;
import com.rrk.common.modules.manage.entity.TbPart;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.manage.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 员工管理前端控制器
 *
 * @author dh
 * @date 2020 6-25
 */
@RestController
@RequestMapping("/manage")
@CrossOrigin
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class EmployeeController {

    @Autowired
    private ITbPartPermissionService partPermissionService;

    @Autowired
    private ITbEmployeeService employeeService;
    @Autowired
    private ITbEmployeePartService employeePartService;

    @Autowired
    private ITbPartService partService;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private ITbPermissionService permissionService;

    private static final String MOBILE_REG = "^[1][3578][0-9]{9}$";

    /**
     * 用户登录
     */
    @PostMapping(value = "/login")
    public R<Object> login(@RequestBody EmployeeDto employeeDto){
       String token =  employeeService.login(employeeDto);
       return R.ok(200,"登录成功",token);
    }

    /**
     * 用户登出
     */


    /**
     * 添加员工（只有超级管理员才有权限）
     */
    @PostMapping(value = "/addEmployee")
    public R<Object> addEmployee(HttpServletRequest request, @RequestBody EmployeeDto employeeDto) throws Exception {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            TbEmployee employee = new TbEmployee();
            //校验是否为手机号
            boolean flag = isMobile(employeeDto.getPhone());
            if (!flag) {
                return R.fail(420, "请输入正确的手机号");
            }
            employee = employeeService.getOne(new QueryWrapper<TbEmployee>().eq("employee_id", employeeDto.getEmployeeId()));
            employee.setEmployeeId(employeeDto.getEmployeeId());
            employee.setUsername(employeeDto.getUsername());
            employee.setPhone(employeeDto.getPhone());
            employee.setEmployeeAddress(employeeDto.getEmployeeAddress());
            employee.setIcon(employeeDto.getIcon());
            employee.setIsUse(employeeDto.getIsUse());
            BCryptPasswordEncoder en = new BCryptPasswordEncoder();
             String password = en.encode(employeeDto.getPassword());
            employee.setPassword(password);
            if (ObjectUtil.isNotNull(employee)) {
                //修改操作
                employee.setUpdateTime(new Date());
                boolean update = employeeService.update(employee, new QueryWrapper<TbEmployee>().eq("employee_id", employeeDto.getEmployeeId()));
                if (update) {
                    return R.ok(200, "修改成功");
                } else {
                    return R.fail(410, "修改失败");
                }
            } else {
                //增加
                employee.setCreateTime(new Date());
                boolean save = employeeService.save(employee);
                if (save) {
                    return R.ok(200, "添加员工成功");
                } else {
                    return R.fail(409, "添加员工失败");
                }
            }
        } else {
            return R.fail(401, "请先登录");
        }
    }

    /**
     * 获取管理员列表
     */
    @GetMapping(value = "/getEmployeeList")
    public R<Object> getEmployeeList(HttpServletRequest request,
                                     @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     @RequestParam(value = "keyword", required = false) String keyword) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            IPage<TbEmployee> page = employeeService.getEmployeeList(pageNo, pageSize, keyword);
            return R.ok(200, "操作成功", page);
        } else {
            return R.fail(401, "请先登录");
        }
    }

    /**
     * 根据用户信息动态获取菜单列表
     */
    @GetMapping(value = "/userInfo")
    public R<Object> userInfo(HttpServletRequest request) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            EmployeeInfo employeeInfo = employeeService.userInfo(employeeId);
            return R.ok(200, "操作成功", employeeInfo);
        } else {
            return R.fail(401, "请先登录");
        }

    }

    /**
     * @param username
     * @return
     */
    private boolean isMobile(String username) {
        Pattern compile = Pattern.compile(MOBILE_REG);
        Matcher matcher = compile.matcher(username);
        boolean matches = matcher.matches();
        return matches;
    }

    /**
     * 根据用户id 删除用户
     */
    @PostMapping(value = "/delEmployee")
    public R<Object> delEmployee(HttpServletRequest request,
                                 @RequestBody DelEmployeeDto delEmployeeDto) {
        Long emId = JwtTokenUtil.getUserId(request);
        if (emId != null) {
            Integer count = employeeService.delEmployee(delEmployeeDto);
            if (count > 0) {
                return R.ok(200, "操作成功");
            }
            return R.fail(421, "删除失败");
        } else {
            return R.fail(401, "请先登录");
        }
    }

    /**
     * getRoleByAdmin 获取该用户已经有哪些角色
     */
    @GetMapping(value = "/getRoleByAdmin")
    public R<Object> getRoleByAdmin(HttpServletRequest request,
                                    @RequestParam(value = "employeeId") Long employeeId) {
        Long emId = JwtTokenUtil.getUserId(request);
        if (emId != null) {
            List<TbPart> list = employeeService.getRoleByAdmin(employeeId);
            return R.ok(200, "操作成功", list);
        } else {
            return R.fail(401, "请先登录");
        }
    }

}
