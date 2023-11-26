package ru.vlasov.taskplannerschedulermvn.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class MessageDto {

    private String email;

    private String title;

    private String body;
}
