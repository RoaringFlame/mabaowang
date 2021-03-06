package com.mabao.controller;

import com.mabao.controller.vo.AddressVO;
import com.mabao.controller.vo.BabyVO;
import com.mabao.controller.vo.JsonResultVO;
import com.mabao.controller.vo.UserInfoVO;
import com.mabao.dao.domain.Address;
import com.mabao.dao.domain.Baby;
import com.mabao.dao.domain.User;
import com.mabao.service.AddressService;
import com.mabao.service.AreaService;
import com.mabao.service.BabyService;
import com.mabao.service.UserService;
import com.mabao.util.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 用户模块
 */
@Controller
@RequestMapping("/user")
public class UserCenterController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private BabyService babyService;
    @Autowired
    private UserService userService;
    @Autowired
    private AreaService areaService;

    /**
     * 个人中心，获取登录用户的基本信息
     *
     * @param model UserInfoVO
     * @return personal页
     */
    @RequestMapping(method = GET)
    public String userCenter(Model model) {
        User user = UserManager.getUser();
        if (user != null) {
            UserInfoVO vo = new UserInfoVO();
            vo.setBabyId(null);
            vo.setBabyName("未添加宝宝");
            Baby baby = this.babyService.findBabyByUserId(user.getId());
            if (baby != null) {
                vo.setBabyId(baby.getId());
                vo.setBabyName(baby.getName());
            }
            User user1 = this.userService.get(user.getId());
            vo.setUserId(user1.getId());
            vo.setUserName(user1.getName());
            vo.setUserPicture(user1.getPicture());
            model.addAttribute("userInfo", vo);
            return "personal";
        } else {
            return "login";
        }
    }

    /**
     * 该用户所有收货地址
     *
     * @param model 地址list
     * @return 收货地址页
     */
    @RequestMapping(value = "/address/userAllAddress", method = GET)
    public String userAllAddress(Model model) {
        User user = UserManager.getUser();
        if (user != null) {
            List<Address> addressList = this.addressService.findUserAllAddress(user.getId());
            model.addAttribute("addressList", addressList);
            return "address";
        } else {
            return "login";
        }
    }

    /**
     * 查某个收货地址详情
     *
     * @param addressId 收货地址ID
     * @param model     地址对象
     * @return 收货地址页
     */
    @RequestMapping(value = "/address/getAddress", method = GET)
    public String getAddress(Long addressId, Model model) {
        Address address = this.addressService.get(addressId);
        model.addAttribute("addressList", address);
        return "chadd";
    }

    /**
     * 添加收货地址
     *
     * @param addressVO 地址VO
     * @return 地址页
     */
    @RequestMapping(value = "/address/addAddress", method = POST)
    public String addAddress(AddressVO addressVO) {
        Address result = this.addressService.addAddress(addressVO);
        if (result != null) {
            return "redirect:userAllAddress";
        } else {
            return "address-failure";
        }
    }

    /**
     * 修改收货地址
     *
     * @param address 地址对象
     * @return 用户地址页
     */
    @RequestMapping(value = "/address/updateAddress", method = POST)
    public String updateAddress(Address address, Model model) {
        address.setArea(this.areaService.get(address.getArea().getId()));
        Address result = this.addressService.updateAddress(address);
        if (result != null) {
            return "redirect:userAllAddress";
        } else {
            return "address-failure";
        }
    }

    /**
     * 删除收货地址
     *
     * @param addressId 地址ID
     * @return 地址页
     */
    @RequestMapping(value = "/address/deleteAddress", method = GET)
    public String removeAddress(Long addressId) {
        this.addressService.deleteAddress(addressId);
        return "redirect:userAllAddress";

    }

    /**
     * 查看某用户宝宝信息
     *
     * @param model 宝宝
     * @return 无宝宝，到新增页；有宝宝，到修改页
     */
    @RequestMapping(value = "baby/allBabyInfo", method = GET)
    public String findAllBabyInfo(Model model) {
        User user = UserManager.getUser();
        if (user != null) {
            Baby baby = this.babyService.findBabyByUserId(user.getId());
            if (baby != null) {
                BabyVO babyVO = BabyVO.generateBy(baby);
                model.addAttribute("babyVO", babyVO);
                return "changemsg";
            } else {
                BabyVO babyVO = new BabyVO();
                model.addAttribute("babyVO", babyVO);
                return "permsg";
            }
        } else {
            return "login";
        }
    }

    /**
     * 新增宝宝信息
     *
     * @param babyInfo 宝宝对象
     * @param model    用户ID
     * @return 宝宝列表接口
     */
    @RequestMapping(value = "baby/addBaby", method = POST)
    public String addBabyInfo(BabyVO babyInfo, Model model) {
        Baby baby = this.babyService.addBaby(babyInfo);
        if (baby != null) {
            BabyVO babyVO = BabyVO.generateBy(baby);
            model.addAttribute("babyVO", babyVO);
            return "changemsg";     //转向个人中心
        } else {
            return "redirect:/user";
        }
    }

    /**
     * 编辑宝宝信息
     *
     * @return 宝宝列表接口
     */
    @RequestMapping(value = "baby/updateBabyInfo", method = POST)
    public String updateBabyInfo(BabyVO babyVO) {
        this.babyService.updateBabyInfo(babyVO);
        return "redirect:/user";
    }

    /**
     * 修改密码首先进行短信验证
     */
    @RequestMapping(value = "/changePwd/sendMes", method = GET)
    public String sendMes(Model model) {
        User tempUser = UserManager.getUser();
        assert tempUser != null;
        User user = userService.get(tempUser.getId());
        String phone = user.getPhone();
        if (phone == null || "".equals(phone)) {
            model.addAttribute("phone", "");
        } else {
            phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            model.addAttribute("phone", phone);
        }
        return "message";
    }

    /**
     * 进入修改密码页面要进行验证码回调判断
     *
     * @param code 验证码
     */
    @RequestMapping(value = "/changePwd", method = GET)
    public String changePwd(String code, Model model) {
        JsonResultVO resultVO = userService.submitCode(3, code, "");
        if (resultVO.getStatus() == JsonResultVO.SUCCESS) {
            return "changepwd";
        } else {
            return "redirect:changePwd/sendMes";
        }
    }

    /**
     * 进入绑定手机页面
     */
    @RequestMapping(value = "/bindphone", method = GET)
    public String bindphone(Model model) {
        User tempUser = UserManager.getUser();
        assert tempUser != null;
        User user = userService.get(tempUser.getId());
        String phone = user.getPhone();
        if (phone == null || "".equals(phone)) {
            model.addAttribute("phone", "");
        } else {
            phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            model.addAttribute("phone", phone);
        }
        return "bindphone";
    }

    @RequestMapping(value = "/consaleNew", method = GET)
    public String conseleNew(Model model) {
        User tempUser = UserManager.getUser();
        assert tempUser != null;
        User user = userService.get(tempUser.getId());
        String phone = user.getPhone();
        if (phone == null || "".equals(phone)) {
            model.addAttribute("phone", "");
        } else {
            phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            model.addAttribute("phone", phone);
        }
        return "consale_new";
    }
}
