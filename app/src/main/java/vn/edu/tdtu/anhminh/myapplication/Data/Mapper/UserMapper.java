package vn.edu.tdtu.anhminh.myapplication.Data.Mapper;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.UserEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.UserDTO;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.User;

public class UserMapper {
    private UserMapper() {

    }

    // DTO -> Entity (For syncing data from the API to Room)
    public static UserEntity toEntity(UserDTO dto){
        if(dto == null) return null;

        return new UserEntity(
                dto.getUserId(),
                dto.getUsername(),
                dto.getPasswordHash(),
                dto.getAvatarImage()
        );
    }

    public static List<UserEntity> toEntityList(List<UserDTO> dtos) {
        if (dtos == null) return null;
        List<UserEntity> list = new ArrayList<>();
        for (UserDTO dto : dtos) {
            UserEntity entity = toEntity(dto);
            if (entity != null) {
                list.add(entity);
            }
        }
        return list;
    }

    // Entity -> Model (Used by Repository to give clean data to Domain/UI)
    public static User toModel(UserEntity entity){
        if (entity == null ) return null;

        User model = new User();
        model.setUserId(entity.getUserId());
        model.setUsername(entity.getUsername());
        model.setAvatarImage(entity.getAvatarImage());
        return model;
    }

    public static List<User> toModelList(List<UserEntity> entities) {
        if (entities == null) return null;

        List<User> list = new ArrayList<>();
        for (UserEntity entity : entities) {
            User model = toModel(entity);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }

    // Model -> Entity (Saving/Updating from Domain/UI to Room)
    public static UserEntity toEntity(User model, String existingPasswordHash) {
        if (model == null) return null;
        //Use the existingPasswordHash provided by the Repository
        // to avoid overwriting a secure value.
        return new UserEntity(
                model.getUserId(),
                model.getUsername(),
                existingPasswordHash,
                model.getAvatarImage()
        );
    }
}