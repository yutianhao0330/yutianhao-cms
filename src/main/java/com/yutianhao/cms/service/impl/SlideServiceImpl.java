package com.yutianhao.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yutianhao.cms.dao.SlideMapper;
import com.yutianhao.cms.domain.Slide;
import com.yutianhao.cms.service.SlideService;

@Service
public class SlideServiceImpl implements SlideService{
	@Autowired
	private SlideMapper slideMapper;

	@Override
	public List<Slide> getSlideList() {
		// TODO Auto-generated method stub
		return slideMapper.getSlideList();
	}
	
}
