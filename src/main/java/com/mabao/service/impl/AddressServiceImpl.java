package com.mabao.service.impl;

import com.mabao.controller.vo.AddressVO;
import com.mabao.dao.domain.Address;
import com.mabao.dao.domain.User;
import com.mabao.dao.repositories.AddressRepository;
import com.mabao.service.AddressService;
import com.mabao.service.AreaService;
import com.mabao.util.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AreaService areaService;

    /**
     * 查默认收货地址
     *
     * @param userId 用户ID
     * @return 地址对象
     */
    @Override
    public Address getDefaultAddress(Long userId) {
        return this.addressRepository.findByUserIdAndState(userId, true);
    }

    /**
     * 显示当前用户的所有收货地址
     *
     * @return 地址list
     */
    public List<Address> findUserAllAddress(Long userId) {
        return this.addressRepository.findByUserId(userId);
    }

    /**
     * 新增收货地址
     *
     * @param address 地址对象
     * @return 新增地址对象
     */
    public Address addAddress(AddressVO address) {
        Address newAddress = new Address();
        User user = UserManager.getUser();
        assert user != null;
        Address defaultAddress = this.addressRepository.findByUserIdAndState(user.getId(), Boolean.TRUE);
        if (defaultAddress != null) {
            defaultAddress.setState(false);
            this.addressRepository.save(defaultAddress);
        }
        newAddress.setState(true);//设为默认地址
        newAddress.setUser(user);
        newAddress.setRecipients(address.getRecipients());
        newAddress.setLocation(address.getLocation());
        newAddress.setTel(address.getTel());
        newAddress.setArea(this.areaService.get(address.getAreaId()));
        return this.addressRepository.save(newAddress);
    }

    /**
     * 更改选中收货地址
     *
     * @param address 地址对象
     * @return 修改的地址对象
     */
    public Address updateAddress(Address address) {
        User user = UserManager.getUser();
        assert user != null;
        if (address.isState()) {     //修改默认地址
            Address defaultAddress = this.getDefaultAddress(user.getId());
            if (defaultAddress != null) {
                defaultAddress.setState(false);
                this.addressRepository.save(defaultAddress);
            }
        }
        return this.addressRepository.saveAndFlush(address);
    }

    /**
     * 删除收货地址
     *
     * @param addressId 地址ID
     */
    public void deleteAddress(Long addressId) {
        this.addressRepository.delete(addressId);
    }

    /**
     * 依据ID获取地址
     *
     * @param addressId 地址id
     * @return 地址对象
     */
    @Override
    public Address get(Long addressId) {
        return this.addressRepository.findOne(addressId);
    }

    /**
     * 修改收货地址默认状态
     *
     * @param addressId 地址对象
     * @return 用户地址页
     */
    @Override
    public Address updateAddressStatus(Long addressId) {
        User user = UserManager.getUser();
        assert user != null;
        Address defaultAddress = this.getDefaultAddress(user.getId());
        if (defaultAddress != null) {
            defaultAddress.setState(false);
            this.addressRepository.saveAndFlush(defaultAddress);
        }
        Address address = this.addressRepository.findOne(addressId);
        address.setState(true);
        return this.addressRepository.saveAndFlush(address);
    }
}
