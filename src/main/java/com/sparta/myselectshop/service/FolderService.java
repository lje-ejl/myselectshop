package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FolderService {

    private final FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {

        List<Folder> existFolders = folderRepository.findAllByUserAndNameIn(user, folderNames);

        List<Folder> folders = new ArrayList<>();

        for (String folderName : folderNames) {
            if (!isExistFolderName(folderName, existFolders)) {
                Folder folder = new Folder(folderName, user);
                folders.add(folder);
            } else {
                throw new IllegalArgumentException("폴더명이 중복되었습니다.");
            }
        }

        folderRepository.saveAll(folders);
    }

    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folders = folderRepository.findAllByUser(user);

        return folders.stream().map(FolderResponseDto::new).toList();
    }

    private boolean isExistFolderName(String folderName, List<Folder> existFolders) {
        for (Folder existFolder : existFolders) {
            if (folderName.equals(existFolder.getName())) {
                return true;
            }
        }
        return false;
    }
}