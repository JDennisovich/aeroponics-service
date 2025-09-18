package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.DTO.ImageData;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDataRO {

    private String fileName;

    private String value;

    private Integer order;

    public ImageData toEntity() {

        return ImageData.builder()
                .fileName(fileName)
                .value(value)
                .order(order)
                .build();
    }
}
