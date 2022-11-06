package com.nekochan.uploadfile.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebResponse<T> {

    private Integer code;

    private String status;

    private T data;

}
