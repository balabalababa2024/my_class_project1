package com.wyr.my_class_project.service.impl;

import com.wyr.my_class_project.entity.Announcement;
import com.wyr.my_class_project.mapper.AnnouncementMapper;
import com.wyr.my_class_project.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    @Transactional
    public Announcement create(Announcement announcement) {
        announcementMapper.insert(announcement);
        return announcement;
    }

    @Override
    @Transactional
    public Announcement update(Announcement announcement) {
        announcementMapper.updateById(announcement);
        return announcementMapper.selectDetail(announcement.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        announcementMapper.deleteById(id);
    }

    @Override
    public Announcement getById(Long id) {
        return announcementMapper.selectDetail(id);
    }

    @Override
    public List<Announcement> getPublished() {
        return announcementMapper.selectPublished();
    }

    @Override
    public List<Announcement> getAll() {
        return announcementMapper.selectList(null);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement != null) {
            announcement.setStatus(status);
            announcementMapper.updateById(announcement);
        }
    }
}
