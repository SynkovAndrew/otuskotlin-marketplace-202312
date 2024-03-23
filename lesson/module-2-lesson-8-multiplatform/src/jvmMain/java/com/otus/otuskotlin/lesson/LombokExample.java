package com.otus.otuskotlin.lesson;

import lombok.Builder;
import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class LombokExample {

    @NotNull
    private String str;
    private int i;
    @Nullable
    private Integer j;

}