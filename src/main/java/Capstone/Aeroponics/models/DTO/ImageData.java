package Capstone.Aeroponics.models.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageData {

    private String fileName;

    private String value;

    private Integer order;
}
