package com.mabao.service.impl;

import com.mabao.dao.domain.Banner;
import com.mabao.dao.repositories.BannerRepository;
import com.mabao.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService{
    @Autowired
    private BannerRepository bannerRepository;

    /**
     * sort倒序查找启用的广告
     * @param status            是否启用
     * @return                  广告对象list
     */
   public List<Banner> findByStatusOrderBySortDesc(boolean status){
       return this.bannerRepository.findByStatusOrderBySortDesc(status);
   }
}
