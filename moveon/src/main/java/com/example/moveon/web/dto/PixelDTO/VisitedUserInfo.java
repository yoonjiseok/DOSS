package com.example.moveon.web.dto.PixelDTO;



import com.example.moveon.web.dto.UserDTO.VisitedUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitedUserInfo {
    private String nickname;
    private String profileImageUrl;

    public static VisitedUserInfo from(VisitedUser visitedUser) {
        return VisitedUserInfo.builder()
                .nickname(visitedUser.getNickname())
                .profileImageUrl(visitedUser.getProfileImage())
                .build();
    }
}
