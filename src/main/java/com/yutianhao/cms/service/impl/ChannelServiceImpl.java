package com.yutianhao.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yutianhao.cms.dao.ChannelMapper;
import com.yutianhao.cms.domain.Channel;
import com.yutianhao.cms.service.ChannelService;
@Service
public class ChannelServiceImpl implements ChannelService{
	@Autowired
	private ChannelMapper channelMapper;

	@Override
	public List<Channel> getChannelList() {
		// TODO Auto-generated method stub
		return channelMapper.getChannelList();
	}

	@Override
	public Channel getChannelById(Integer id) {
		// TODO Auto-generated method stub
		return channelMapper.getChannelById(id);
	}
	
}
