package com.example.moveon.web.dto.PixelDTO;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import com.example.moveon.domain.Pixel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class IndividualHistoryPixelInfoResponse {
    private String address;
    private Integer addressNumber;
    private Integer visitCount;
    private List<LocalDateTime> visitList;

    public static IndividualHistoryPixelInfoResponse from(Pixel pixel, List<LocalDateTime> visitList) {
        String realAddress;

        if (pixel.getAddress() != null) {
            String[] addressArr = pixel.getAddress().split(" ");
            if (addressArr[0].equals("대한민국")) {
                realAddress = addressArr[0];
            } else {
                realAddress = String.join(" ", Arrays.copyOfRange(addressArr, 1, addressArr.length));
            }
        } else {
            realAddress = null;
        }

        return IndividualHistoryPixelInfoResponse
                .builder()
                .address(realAddress)
                .addressNumber(pixel.getAddressNumber())
                .visitCount(visitList.size())
                .visitList(visitList)
                .build();
    }
}
