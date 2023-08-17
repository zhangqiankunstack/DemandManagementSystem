package com.rengu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.FileModel;
import com.rengu.enums.FileMapper;
import com.rengu.service.FileService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileModel> implements FileService {
}
